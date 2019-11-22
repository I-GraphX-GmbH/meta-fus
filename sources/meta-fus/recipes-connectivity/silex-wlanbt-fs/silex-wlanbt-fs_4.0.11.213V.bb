DESCRIPTION = "Add package silex-wlan-fs for F&S boards"
LICENSE = "CLOSED"

inherit module

kfirmdir = "/lib/firmware"
kmoddir = "/lib/modules/${KERNEL_VERSION}"
SRC_URI = " http://source.codeaurora.org/external/wlan/qcacld-2.0/snapshot/${PV}.tar.gz \
			file://S02silex \
		  	file://0001-qcacld-2.0-Fix-build-error-in-third-party-platform.patch \
		  	file://0002-qcacld-2.0-Don-t-define-pci-related-api-if-HIF_PCI-i.patch \
			file://0003-Do-not-allocate-RAM-dump-buffer.patch \
"

SRC_URI[md5sum] = "0be53c5477c2293e50b81f54e0589d75"
SRC_URI[sha256sum] = "96ac8fb9bf065fc2b999e0112a309953a52ab7aaa66185efcdee67f7ab41a5f7"

S = "${WORKDIR}/${PV}"

EXTRA_OEMAKE += " \
		WLAN_ROOT=${S}\
		MODNAME=wlan \
		CONFIG_QCA_WIFI_ISOC=0 \
		CONFIG_QCA_WIFI_2_0=1 \
		CONFIG_QCA_CLD_WLAN=m \
		WLAN_OPEN_SOURCE=1 \ 
		CONFIG_CLD_HL_SDIO_CORE=y \
		CONFIG_LINUX_QCMBR=y \
		SAP_AUTH_OFFLOAD=1 \ 
		CONFIG_QCA_LL_TX_FLOW_CT=1 \
		CONFIG_WLAN_FEATURE_FILS=y \
		CONFIG_FEATURE_COEX_PTA_CONFIG_ENABLE=y \
		CONFIG_QCA_SUPPORT_TXRX_DRIVER_TCP_DEL_ACK=y \
		CONFIG_WLAN_WAPI_MODE_11AC_DISABLE=y \
		CONFIG_WLAN_WOW_PULSE=y \
"

# recipe fus-silex may installed after wpa-supplicant and hostapd are already
# installed. The reason is fus-silex overwrites some binarys of wpa-supplicant
# and hostapd so this recipes must be installed before fus-silex installed.
RDEPENDS_${PN} += "wpa-supplicant hostapd"

do_install() {
    install -d ${D}${kmoddir}/extra
    install -d ${D}${sysconfdir}/rc1.d
    install -d ${D}${sysconfdir}/rc2.d
    install -d ${D}${sysconfdir}/rc3.d
    install -d ${D}${sysconfdir}/rc4.d
    install -d ${D}${sysconfdir}/rc5.d
	install -d ${D}${sysconfdir}/init.d
# install modules

    install -m 0644 ${S}/wlan.ko ${D}${kmoddir}/extra

# install startscript
    install -m 0755 ${WORKDIR}/S02silex ${D}${sysconfdir}/init.d/
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc1.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc2.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc3.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc4.d/S02silex
    ln -sf ../init.d/S02silex  ${D}${sysconfdir}/rc5.d/S02silex
}

FILES_${PN} += "${kmoddir}/extra-/wlan.ko"
FILES_${PN} += "${sysconfdir}/init.d/S02silex"
FILES_${PN} += "${sysconfdir}/rc1.d \
				${sysconfdir}/rc2.d \
				${sysconfdir}/rc3.d \
				${sysconfdir}/rc4.d \
				${sysconfdir}/rc5.d \
				${sysconfdir}/init.d \
"
