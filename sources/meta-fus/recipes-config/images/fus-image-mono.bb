DESCRIPTION = "F&S Image, adds Qt5 to F&S standard image"
LICENSE = "MIT"

require recipes-config/images/fus-image-std.bb

inherit distro_features_check
CONFLICT_DISTRO_FEATURES += " fb"
IMAGE_INSTALL += "mono nuget tzdata libgdiplus"

DEFAULT_TEST_SUITES_pn-${PN} = "mono ssh ping"
