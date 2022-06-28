FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Remove NXP patches
SRC_URI_remove = " \
		file://0001-bluetooth-Add-bluetooth-support-for-QCA6174-chip.patch \
		file://0002-hciattach-set-flag-to-enable-HCI-reset-on-init.patch \
		file://0003-hciattach-instead-of-strlcpy-with-strncpy-to-avoid-r.patch \
		file://0004-Add-support-for-Tufello-1.1-SOC.patch \
		file://0005-bluetooth-Add-support-for-multi-baud-rate.patch \
"
# Add Silex patches 
SRC_URI += " \
		file://0001-QCA9377-HCI-Uart-Support.patch \
		file://0002-QCA9377-UART-H4-IBS-Support-Baudrate-Fix.patch \
		file://0003-QCA9377-UART-Skip-NVM-BDADDR.patch \
		file://0004-Obex-Set-DBUS-Session-Service.patch	\
		file://0005-Bluetoothd-Service-Enable-Compatibility-Mode.patch \
		file://0006-Fix-GATT-Glib-WARNING.patch \
"
# We disable the automatic start of the bluetooth.service, because
# it starts the bluetoothd daemon, which at this stage tries to
# get the host name via hostnamed.service. The net namespace feature
# is not activated in the Linux Kernel by now, so it fails.
# The bluetoothd daemon has to be stated by hand after boot up.

inherit systemd
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
