fsimx6-Y0.2
===========

This is the second F&S release for Yocto. At the moment it is only
available for the efusA9 module (imx6dl-efusa9) and armStoneA9
single-board computer (imx6dl-armstonea9) with the single or dual core
version of the i.MX6 CPU (Solo or DualLite). Other boards, modules and
CPU variants will follow in the future.

This release is based on the Freescale fsl-community-bsp release of
Yocto which in turn is based on Yocto 1.6.1 (Daisy). It provides a
U-Boot based on u-boot-2014.01 and a Linux kernel based on 3.10.17.

The following standard images were tested:

1. core-image-minimal          (5 MB UBIFS)
2. core-image-base	       (30 MB UBIFS)
3. core-image-sato             (77 MB UBIFS)
4. core-image-x11              (64 MB UBIFS)
5. core-image-clutter          (82 MB UBIFS)
6. fsl-image-multimedia        (159 MB UBIFS)
7. fsl-image-multimedia-full   (167 MB UBIFS)
8. fsl-image-machine-test      (300 MB UBIFS)


About Yocto
-----------

Yocto is a system to build a Linux distribution. So it is *not* a
Linux distribution by itself, but it will help *building* a Linux
distribution. This includes the toolchains, the bootloader, the
kernel, the root filesystems and Yocto can even build a package
repository where the end user can install additional packages from.

Yocto is organized in layers. Layers may exist next to each other, but
usually they have a hierarchical order, where the next layer is based
on the previous layer. The F&S yocto release is actually only the
Freescale Yocto Community BSP release, where an additional F&S layer
is added on top to provide support for the boards and modules from
F&S. The Freescale Community BSP release itself is based on the
regular Yocto release and simlpy adds some Freescale layers, to
support the Freescale CPUs and Evaluation Boards. Yocto itself is
again based on Open Embedded, and adds some additional Yocto specific
software layers, called "poky".

So at the core, Yocto is an Open Embedded based system. Open Embedded
uses recipes, like they are used when baking a cake. The recipe
describes what you need as ingredients, and then it tells step by step
what tasks have to be done to produce the cake. So a recipe in Yocto
also names the ingredients (source packages, patch files,
configuration settings, etc.) and then tells step by step what tasks
have to be done to produce the target. For example how sources are
unpacked and patched, how they are "converted" to binaries, how tools
are installed to help the build process, how different files are
grouped to root filesystems or other images, and so on. To add new
packages or build strategies, you simply have to add some more
recipes. This makes Yocto/Open Embedded a rather powerful build system
where you can have any influence that you like.

On the other hand you actually have to write a seperate recipe even
for small tasks. Even if you just want to assemble your own set of
packages to build your private root filesystem, you need to write a
recipe. There is no easy menu or GUI based package selection like for
example in Buildroot. Everything works with recipes. Of course you can
build some default images, but at the moment where you want to do
something different, you have to modify an existing recipe or you have
to write your own one.

So you actually have to learn how to write recipes if you want to work
with Yocto. There is no way around this. It may also be necessary to
learn how configurations and layers are written, because the most
simple way to add your own application is by adding a separate layer
for it, on top of the provided Yocto eco system.

These recipes use an own skripting language, based on Python. And
there is a program called bitbake that can parse these recipes and
execute the steps described in them. So building a Yocto package, root
filesystem or even full target image as a whole, you have to use
bitbake.

At the first invocation bitbake parses all existing recipes and builds
a cache for them. This cache speeds up later calls considerably,
because parsing the recipes can already take a few minutes and this
reduces to a few seconds when the cache can be re-used. Then bitbake
determines what tasks have to be done to reach the desired target. A
task may be fetching a package via the internet, unpack a package,
configure, compile, install a package or similar steps. It then
generates a list of these tasks and then executes them.

If you have a multi-core system, bitbake can work in parallel. It
knows the dependencies between the tasks and if possible, it already
executes the next task(s) in parallel, up to a certain limit that can
be given in the configuration. But please keep in mind that each task
itself may be a complicated build process that may use parallel
execution, for example a "make" with many parallel threads. So don't
overdo the settings, because the number of parallel bitbake tasks will
multiply with the number of parallel executions of the tasks
themselves.

If execution fails at some task, bitbake finishes all the other tasks
that are running in parallel, but it will not start any new tasks.
When all pending tasks are completed, it will stop with an error
message. bitbake logs all steps that it does in log files, one file
per task. So you can reconstruct at which step the build process
failed. Then you can fix the problem and when you call bitbake again,
it will skip the tasks that it had already finished successfully and
restart at the point where it had left off.

You can also interrupt the build process by hand, by typing Ctrl-C.
However bitbake will *not* stop immediately. Instead like in the error
case, it first finishes all running tasks and then stops. You can
continue by issuing the bitbake command again.


Installation
------------

First of all, unpack the F&S Yocto release. Yocto-only releases are
marked with a 'Y' instead of a 'V' for the release version number.

  tar xvf fsimx6-Y0.2.tar.bz2
  cd fsimx6-Y0.2

As mentioned above, the F&S Yocto release uses different layers from
different sources. When installing, these layers are actually
downloaded from different GIT repositories. Only the F&S layer is
already here inside the F&S Yocto release tar archive. This layer
simply provides the U-Boot and Linux kernel for all supported F&S
boards.

To download the remaining layers, simply call this skript:

  ./yocto-download

This will take a few minutes, depending on your internet speed.

The download is done by using a small tool called "repo" that is
designed to handle several GIT repositories in parallel. So the first
step of the skript is to download the repo tool. Then it fetches the
repo description for the Freescale Community BSP release and finally
lets repo download all the different GIT repositories with the Yocto
layers.

- fsl-community-bsp-base
- meta-fsl-arm
- meta-fsl-arm-extra
- poky
- meta-fsl-demos
- meta-openembedded

At the end it simply adds the F&S layer (called meta-f+s) to the
configuration file bblayers.conf.


Configure for a board
---------------------

To configure for a specific F&S board, simply call

  MACHINE=<board-name> . setup-environment <build-dir>

Please note the extra dot that is used to run the setup-environment in
the current shell by sourcing it and not spawning a new sub-shell.

<board-name> can be one of the supported boards, which is currently only
imx6dl-efusa9 or imx6dl-armstonea9. <build-dir> is the name of the
build directory to create. We recommend a name that starts with
"build", e.g. "build-efusa9". This command will automatically switch
to the newly created directory and sets the environment so that
everything runs smoothly. You have to repeat this step in every new
shell that should be used to build this Yocto image. For example if
you restart your PC, you have to call this command again to set up the
environment correctly again.


Build an image
--------------

To build one of the demo images, simply call

  bitbake <image-name>

Here <image-name> is one of the images listed above, for example
core-image-minimal. This will download all the required source
packages, patch them, configure them, compile them, and assemble them
in a package system and in target images. This includes binaries for
the bootloader, the Linux kernel and the root filesystem. Even the
toolchain, that is used for compiling these packages is built during
this process. Therefore building can take quite a long time, even
several hours. For example the rather complex fsl-image-machine-test
took about 6 hours on a PC with a rather fast quad core CPU with
hyper-threading. It also requires quite a lot of free disk space. We
recommend having at least 50GB of free disk space, the more the
better.

The resulting files can be found in

  tmp/deploy/images

The names are as follows. Please replace <board-name> with the board
name that you configured your Yocto system with, e.g. imx6dl-efusa9 or
imx6dl-armstonea9, and <image-name> with the name of the target image
that you have built.

  uboot.nb0                          U-Boot binary (suited for NBoot)
  uImage                             Linux kernel image for <board-name>
  uImage-<board-name>.dtb            Device tree for <board-name>
  <image-name>-<board-name>.ubifs    Image to be installed in NAND flash
  <image-name>-<board-name>.ext3     ext3 image, e.g. to be used via NFS
  <image-name>-<board-name>.sdcard   Image to be installed on an SD card

If the build process fails at some time, you can fix the error and
continue by calling the bitbake command again. This will skip all the
steps that were already done and only completes the missing stages.

Usually you can also build additional images without having to erase
the previous results. Just call bitbake with the new image name. Again
only those steps that are new for the new image are done by bitbake.
However you have to keep in mind that in some cases the compilation
has to be done differently for different images and that some already
completed task may not fit. For example some graphical programs have
to be configured differently, if Wayland or DirectFB is used instead
of the standard X.org X-Server. Then you would have to re-do such
tasks by hand and it is rather difficult to tell which ones. So if an
image fails to build or run, it may be better to restart the build
process completely anew.

Remark:

Some images are rather large and may not fit into the NAND flash
memory of your board or module. So for example fsl-image-multimedia
results in a file of about 160MB which does not fit into the 128MB of
the regular armStoneA9, so it can not be run from NAND flash. Such a
rootfs can only be mounted via NFS or SD card then.

In addition, the fsl-image-machine-test creates an UBIFS image with a
size of about 300MB. The UBIFS images are configured to be at most
256MB of size. To compile this image, you have to modify the file

  sources/meta-fsimx6/conf/machine/<board-name>.conf

to avoid an overflow of the UBIFS rootfs. Replace the line

  MKUBIFS_ARGS = "-m 2048 -e 126976 -c 2048"

with this content:

  MKUBIFS_ARGS = "-m 2048 -e 126976 -c 2800"

We have included the U-Boot binary, the kernel image, the kernel
device tree binary and a few root filesystems images in the binaries
subdirectory. You can download them directly to the board to try them
out. The default display is LCD with a resolution of 800x480 pixels.


Rebuild a single package
------------------------

bitbake needs a recipe as target name. Target images are also only
recipes. But you can also execute smaller entities, for example a
smaller sub-target or even a single package. Everything that has a
separate recipe can be processed separately with bitbake.

Usually when a package is built, different stages are executed. For
example

  fetch, unpack, patch, configure, compile, strip, install,
  package, deploy

The list may vary from package to package. For example when building a
Linux kernel, there are further steps to build the kernel modules and
the device tree binaries. In addition there is also always the special
stage named "clean" to remove a package from the system, for example
to rebuild it. 

Therefore to rebuild a single package, you usually execute the
following sequence of commands. They clean and recompile the package
and then add it again to the final target image.

  bitbake -c clean <package>
  bitbake -c compile <package>
  bitbake <image-name>


Install U-Boot on the board
---------------------------

To install the U-Boot image on the board, start the board and go into
NBoot. This is done by pressing 's' (lower-case S) until it repeats,
then restart the board. Use a terminal program that supports 1:1
binary download, for example DCUTermi from F&S or Tera Term.

If you are coming from a different environment, for example from a
Buildroot based environment, we recommend erasing the whole NAND flash
with command 'E' (upper-case e) here.

Now press 'd' (lower-case D) to start the serial download. Then send
the file "uboot.nb0" to the board. After download is complete, press
'f' (lower-case F) to save U-Boot to NAND flash. Now restart the board
or simply press 'x' (lower-case X) to start U-Boot directly from the
loaded image in RAM.


Install kernel image and rootfs
-------------------------------

On the PC, the images have to be copied to a directory from where they
can be downloaded via TFTP. So you need to have a TFTP server running
on your PC. When you are following the instructions of the F&S
documentation "AdvicedForLinuxOnPC_eng.pdf" for how to do this, you
have a directory /tftpboot for this purpose. So copy the following
files to it:

  cp uImage /tftpboot
  cp uImage-<board-name>.dtb /tftpboot
  cp <image-name>-<board-name>.ubifs /tftpboot

Now on your board go to U-Boot. This is done by pressing a key while
the Autostart countdown counts down. If you have not done it already,
set the correct network configuration variables, i.e. ethaddr,
gateway, netmask, and of course serverip to the IP address of your PC.
Save these settings with saveenv.

Download and save the kernel image:

  tftp uImage
  nand erase.part Kernel
  nand write $loadaddr Kernel $filesize

Download and save the device tree data:

  tftp uImage-<board-name>.dtb
  nand erase.part FTD
  nand write $loadaddr FDT $filesize

Download and save the rootfs image:

  ubi part TargetFS
  tftp <image-name>-<board-name>.ubifs
  ubi write $loadaddr rootfs $filesize

Finally make sure that the boot strategy is set up for device trees.

  run _kernel_nand
  run _rootfs_ubifs
  run _fdt_nand
  saveenv

Now start the linux system with "boot" or simply restart the board.


Useful utilities
----------------

If you run

  source ../yocto-f+s-utilities

in the build directory, you will get the following small helper
functions:

list_f+s_target_boards      Show a list of all supported F&S boards
list_target_images          Show all recipes with "image" in their name
list_all_recipes            List all available recipes (list is long!)
list_target_image_packages  List all the packages that are part of the
                            target image given as argument

For example to determine which packages are part of core-image-sato,
call

  list_target_image_packages core-image-sato


Further reading
---------------

There is quite a lot of documentation available at the Yocto homepage

  https://www.yoctoproject.org/documentation
  https://www.yoctoproject.org/documentation/active

In addition you can also have a look at the Freescale documentation on
github:

  https://github.com/Freescale/Documentation
