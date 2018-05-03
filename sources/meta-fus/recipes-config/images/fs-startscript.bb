DESCRIPTON = "F&S Startup scripts"
LICENSE = "MIT"

#default license in /mnt/yocto/fsl-release-bsp-l4.1.15_2.0.0-ga/sources/poky/LICENSE
#LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r0"

SRC_URI = "file://fsdistro-wayland.sh \
	   file://fsdistro-x11.sh \
	   file://fsalias.sh \
	   file://S01fssetup \
	   file://fsgetty \
"

HAS_WAYLAND = "${@bb.utils.contains("DISTRO_FEATURES", "wayland", "yes", "no", d)}"

do_install() {
    install -d ${D}${sysconfdir}/profile.d/
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/rc1.d
    install -d ${D}${sysconfdir}/rc2.d
    install -d ${D}${sysconfdir}/rc3.d
    install -d ${D}${sysconfdir}/rc4.d
    install -d ${D}${sysconfdir}/rc5.d
    install -d ${D}${base_sbindir}

    install -m 0755 ${WORKDIR}/fsalias.sh   ${D}${sysconfdir}/profile.d/
    
    if [ "${HAS_WAYLAND}" = "yes" ]; then
	install -m 0755 ${WORKDIR}/fsdistro-wayland.sh  ${D}${sysconfdir}/profile.d/
    else
	install -m 0755 ${WORKDIR}/fsdistro-x11.sh  ${D}${sysconfdir}/profile.d/
    fi

    install -m 0755 ${WORKDIR}/S01fssetup   ${D}${sysconfdir}/init.d/
    ln -sf ../init.d/S01fssetup  ${D}${sysconfdir}/rc1.d/S01fssetup
    ln -sf ../init.d/S01fssetup  ${D}${sysconfdir}/rc2.d/S01fssetup
    ln -sf ../init.d/S01fssetup  ${D}${sysconfdir}/rc3.d/S01fssetup
    ln -sf ../init.d/S01fssetup  ${D}${sysconfdir}/rc4.d/S01fssetup
    ln -sf ../init.d/S01fssetup  ${D}${sysconfdir}/rc5.d/S01fssetup

    install -m 0755 ${WORKDIR}/fsgetty   ${D}${base_sbindir}/
}
