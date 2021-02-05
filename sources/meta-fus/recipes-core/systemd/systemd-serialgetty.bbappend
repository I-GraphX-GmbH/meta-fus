do_install() {
	if [ "${SERIAL_CONSOLES}" == "FUS_LOGIN_CONSOLE" ] ; then
		install -d ${D}${systemd_unitdir}/system/
		install -m 0644 ${WORKDIR}/serial-getty@.service ${D}${systemd_unitdir}/system/fsserial-getty@.service 
	else
		if [ ! -z "${SERIAL_CONSOLES}" ] ; then
			default_baudrate=`echo "${SERIAL_CONSOLES}" | sed 's/\;.*//'`
			install -d ${D}${systemd_unitdir}/system/
			install -d ${D}${sysconfdir}/systemd/system/getty.target.wants/
			install -m 0644 ${WORKDIR}/serial-getty@.service ${D}${systemd_unitdir}/system/
			sed -i -e s/\@BAUDRATE\@/$default_baudrate/g ${D}${systemd_unitdir}/system/serial-getty@.service

			tmp="${SERIAL_CONSOLES}"
			for entry in $tmp ; do
				baudrate=`echo $entry | sed 's/\;.*//'`
				ttydev=`echo $entry | sed -e 's/^[0-9]*\;//' -e 's/\;.*//'`
				if [ "$baudrate" = "$default_baudrate" ] ; then
					# enable the service
					ln -sf ${systemd_unitdir}/system/serial-getty@.service \
						${D}${sysconfdir}/systemd/system/getty.target.wants/serial-getty@$ttydev.service
				else
					# install custom service file for the non-default baudrate
					install -m 0644 ${WORKDIR}/serial-getty@.service ${D}${systemd_unitdir}/system/serial-getty$baudrate@.service
					sed -i -e s/\@BAUDRATE\@/$baudrate/g ${D}${systemd_unitdir}/system/serial-getty$baudrate@.service
					# enable the service
					ln -sf ${systemd_unitdir}/system/serial-getty$baudrate@.service \
						${D}${sysconfdir}/systemd/system/getty.target.wants/serial-getty$baudrate@$ttydev.service
				fi
			done
		fi
	fi
}
