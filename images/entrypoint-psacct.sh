#!/bin/bash

# Just die if not root.
if [ ${UID} -ne 0 ]; then
    echo "FATAL: Need to run as root."
    exit 1
fi

# Die if not running with CAP_SYS_PACCT
if [ -z "$(capsh --decode=$(cat /proc/self/status | grep ^CapEff | cut -f2) | grep sys_pacct)" ]; then
    echo "FATAL: Need CAP_SYS_PACCT to run."
    exit 1
fi

# Die if running as PID 1 (no access to host PID namespace)
if [ $$ -eq 1 ]; then
    echo "FATAL: Need access to host PID namespace, can't seriously be PID 1."
    exit 1
fi

# Shutdown handler.
trap_shutdown() {
    echo "Shutting down..."
    exit 0
}
trap trap_shutdown SIGHUP SIGINT SIGQUIT SIGABRT SIGTERM

# Announce collection period.
PERIOD=10
if [ -n "$1" ]; then
    PERIOD=$1
fi
echo "Reporting on process activity every ${PERIOD} seconds."

# Did someone ask for cumulative stats?
case ${CUMULATIVE:-0} in
    1|yes|true)
	echo "Gathering cumulative accounting data since container start."
	CUMULATIVE=1
	;;
    *)
	echo "Clearing accounting data between snapshots."
	CUMULATIVE=0
	;;
esac

# Clean up /var/account/pacct on startup if so requested.
case ${STARTUP_SCRATCH:-0} in
    1|yes|true)
	echo "Removing existing accounting data due to scratch-on-startup being ${STARTUP_SCRATCH}..."
	rm -f /var/account/pacct*
	;;
    *)
	echo "Skipping scratch-on-startup..."
	;;
esac

# Make sure we have everything
echo "Making sure /var/account/pacct is ready..."
/usr/libexec/psacct/accton-create

# Start accounting
/usr/sbin/accton /var/account/pacct

# Do the loop
while [ 1  ]; do
    sleep ${PERIOD}

    # Report after waking up.
    #	Looks like the choice is between paging and I/O, can't have both.

    # This one prints paging info...
    /usr/sbin/sa -ajlp > /var/account/psacct-dump-all

    # ...and this one prints total I/O operations (which seems to be unsupported).
    #/usr/sbin/sa -ajlD > /var/account/psacct-dump-all

    # This is less than very useful.
    #/usr/sbin/dump-acct /var/account/pacct > /var/account/psacct-dump-raw

    # Could make a CSV out of the above "sa":
    #   | tr -s' ' | sed 's/^ //; s/ /,/g'

    # Did we say NOT cumulative data?
    if [ ${CUMULATIVE} -eq 0 ]; then
	echo -n > /var/account/pacct
    fi
done

# End of entrypoint-psacct.sh
