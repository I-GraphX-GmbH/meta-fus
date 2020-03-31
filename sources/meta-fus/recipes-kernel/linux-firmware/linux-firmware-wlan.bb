SUMMARY = "Firmware files for Marvell SD8997"
SECTION = "kernel"

LICENSE = "\
   Firmware-Marvell \
"

LIC_FILES_CHKSUM = "\
	    file://LICENCE.Marvell;md5=9ddea1734a4baf3c78d845151f42a37a \
"
NO_GENERIC_LICENSE[Firmware-Marvell] = "LICENCE.Marvell"

SRCREV = "1baa34868b2c0a004dc595b20678145e3fff83e7"
PE = "1"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git"

UPSTREAM_VERSION_UNKNOWN = "1"

S = "${WORKDIR}/git"

inherit allarch

CLEANBROKEN = "1"

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/mrvl
	install -m 0755 mrvl/sdsd8997_combo_v4.bin ${D}/lib/firmware/mrvl
	ln -s sdsd8997_combo_v4.bin ${D}/lib/firmware/mrvl/sd8997_uapsta.bin
}

PACKAGES =+ "${PN}-sd8997 "

RDEPENDS_${PN}-sd8997 += "linux-firmware-marvell-license"

LICENSE_${PN}-sd8997 = "Firmware-Marvell"

FILES_${PN}-sd8997 = " \
  /lib/firmware/mrvl/sdsd8997_combo_v4.bin \
  /lib/firmware/mrvl/sd8997_uapsta.bin \
"

FILES_${PN}-license += "/lib/firmware/LICEN*"

RDEPENDS_${PN} += "linux-firmware-license"
RDEPENDS_${PN} += "linux-firmware-whence-license"

# Firmware files are generally not ran on the CPU, so they can be
# allarch despite being architecture specific
INSANE_SKIP = "arch"
