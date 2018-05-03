DESCRIPTION = "Add package silex-wlan-fs for F&S boards"
LICENSE = "CLOSED"

inherit module-base

kfirmdir = "/lib/firmware"
kmoddir = "/lib/modules/${KERNEL_VERSION}"
SRC_URI = "file://silex-wlan-fs-v4.1_01272017.tar.bz2 \
           file://S02silex \
          "
SILEX = "${WORKDIR}/silex-wlan-fs-v4.1_01272017"

# skip ldflags because it occurs a QA Issue: No GNU_HASH in the elf binary
INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

# Error "files/directory were installed but not shipped" occurs. so the files
# have to be added to the package.
FILES_${PN} += "${kmoddir}/silex-wlan ${kfirmdir}"

# recipe fus-silex may installed after wpa-supplicant and hostapd are already
# installed. The reason is fus-silex overwrites some binarys of wpa-supplicant
# and hostapd so this recipes must be installed before fus-silex installed.
RDEPENDS_${PN} += "wpa-supplicant hostapd"

do_install() {
    install -d ${D}${kmoddir}/silex-wlan
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/rc1.d
    install -d ${D}${sysconfdir}/rc2.d
    install -d ${D}${sysconfdir}/rc3.d
    install -d ${D}${sysconfdir}/rc4.d
    install -d ${D}${sysconfdir}/rc5.d
    install -d ${D}${kfirmdir}
    install -d ${D}${sbindir}
    install -d ${D}${bindir}

# install modules
    install -m 0644 ${SILEX}/modules/compat.ko ${D}${kmoddir}/silex-wlan
    install -m 0644 ${SILEX}/modules/sxcfg80211.ko ${D}${kmoddir}/silex-wlan
    install -m 0644 ${SILEX}/modules/wlan.ko ${D}${kmoddir}/silex-wlan

# install firmware
    cp -r ${SILEX}/firmware/* ${D}/lib/firmware/

# install own wpa supplicant files
    install -m 0755 ${SILEX}/binaries/wpa_cli ${D}${sbindir}
    install -m 0755 ${SILEX}/binaries/wpa_supplicant ${D}${sbindir}
    install -m 0755 ${SILEX}/binaries/wpa_passphrase ${D}${bindir}

# install own hostapd files
    install -m 0755 ${SILEX}/binaries/hostapd_cli ${D}${sbindir}
    install -m 0755 ${SILEX}/binaries/hostapd ${D}${sbindir}

# install startscript
    install -m 0755 ${WORKDIR}/S02silex ${D}${sysconfdir}/init.d/
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc1.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc2.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc3.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc4.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc5.d/S02silex
}
