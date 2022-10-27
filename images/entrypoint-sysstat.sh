#!/bin/bash

# Just die if not root.
if [ ${UID} -ne 0 ]; then
    echo "FATAL: Need to run as root."
    exit 1
fi

# Announce collection period.
PERIOD=10
if [ -n "$1" ]; then
    PERIOD=$1
fi
REPORT_LENGTH=$((2 * PERIOD - 1))
echo "Collecting and reporting system activity statistics every ${PERIOD} seconds..."

# Clean up /var/log/sa on startup if so requested.
case ${STARTUP_SCRATCH:-0} in
    1|yes|true)
	echo "Removing existing SA1 data due to scratch-on-startup being ${STARTUP_SCRATCH}..."
	rm -rf /var/log/sa
	;;
    *)
	echo "Skipping scratch-on-startup..."
	;;
esac

# Add a record of rotation to existing logs if so requested.
case ${STARTUP_ROTATE:-0} in
    1|yes|true)
	echo "Marking existing SA1 data as rotated due to rotate-on-startup being ${STARTUP_ROTATE}..."
	/usr/lib64/sa/sa1 --rotate
	;;
    *)
	echo "Skipping rotate-on-startup..."
	;;
esac

# Mark a reboot.
/usr/lib64/sa/sa1 --boot

# Remember the hour-of-day.
PREV_HOUR="$(date +%H)"

# Do the loop.
while [ 1 ]; do
    # Start sa1.
    /usr/lib64/sa/sa1 ${PERIOD} 1

    # Produce the report.
    REPORT_FROM=$(date +%H:%M:%S -d "${REPORT_LENGTH} seconds ago")
    REPORT_UNTIL=$(date +%H:%M:%S)
    /usr/bin/sadf -s ${REPORT_FROM} -e ${REPORT_UNTIL} -j -- -A > /var/log/sa/dump.json

    # Have we awoken in a new day? Rotation record, please!
    NEW_HOUR="$(date +%H)"
    if [ ${NEW_HOUR} -lt ${PREV_HOUR} ]; then
	echo "Marking last saXX as rotated..."
	/usr/lib64/sa/sa1 --rotate
    fi
    PREV_HOUR=${NEW_HOUR}

    sleep ${PERIOD}
done

# End of entrypoint-sysstat.sh
