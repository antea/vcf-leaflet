#!/bin/zsh

# This script performs all the steps that are needed to release and deploy a new version of the vcf-leaflet.

setopt shwordsplit

autoload colors && colors
r="$reset_color"
star="$fg_bold[blue]** $r"
red="$fg_bold[red]"
green="$fg_bold[green]"
yellow="$fg_bold[yellow]"
blue="$fg_bold[blue]"
error="${star}${red}"
question="${star}${yellow}"
info="${star}${green}"
instruction="${star}${blue}"

# The profiles to use when building.
PROFILES="directory"

# Verifies if a certain executable file exists.
# $1 name of the executable.
function assert_executable() {
  which "$1" >/dev/null
  if [ "$?" -ne 0 ]; then
    print "${star}${red}We need $1 to run this script. Quitting.$r"
    print "${star}${red}Try with:$r"
    print "${star}${red} brew install $1        (on mac)$r"
    print "${star}${red} apt-get install $1     (on linux)$r"
    exit 1
  fi
}

# Informative text is green.
# Instructions to the user are blue.
echo "${info}GREEN if for Information ${instruction}BLUE is for Instructions$r"
echo

echo "${instruction}Please shut down your IDE and hit a key to continue.$r"
read -r -k1 RESULT
echo

if [[ $1 == "-h" ]]; then
  # shellcheck disable=SC2154
  echo "${instruction}Usage: ./release.sh$r"
  echo "${instruction}The version number is guessed automatically from the pom.xml.$r"
  exit
fi

###############################################################################
# Preliminary checks
###############################################################################

assert_executable git
assert_executable mvn
assert_executable mvnd

# Find the version declared by the pom.xml descriptor
RAW_VERSION=$(mvn help:evaluate -q -DforceStdout -N -Dexpression=project.version)
if [[ $RAW_VERSION == *-SNAPSHOT ]]; then
  echo "${info}Current version is $RAW_VERSION.$r"
else
  echo "${star}${error}Current version is not a SNAPSHOT. Quitting.$r"
  exit 1
fi

# Remove the -SNAPSHOT suffix
VERSION=${RAW_VERSION//-SNAPSHOT/}
echo "${info}Releasing version $VERSION.$r"
echo

# Calculate the next version (i.e. increment the version number)
NEXTVERSION=$(echo "$VERSION" | awk -F. -v OFS=. '{print $1"."$2"."$3"."($4+1)}')
echo "${info}Next version will be $NEXTVERSION-SNAPSHOT.$r"
echo

CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

# Check whether we are in the branch we are supposed to be (the vaadin24/ branch for the declared version)
if [[ $CURRENT_BRANCH != "vaadin24" ]]; then
  echo "${star}${error}Releases can only be made from the vaadin24 branch. Current branch is $CURRENT_BRANCH.$r"
  exit 1
else
  echo "${info}Good, we are in the vaadin24 branch: $CURRENT_BRANCH$r"
fi
echo

###############################################################################
# Version bump
###############################################################################

# Updating the POMs
echo "${info}Updating POMs to version ${VERSION}.$r"
mvnd versions:set -DautoVersionSubmodules=true -P${PROFILES} -DnewVersion="${VERSION}"
echo "${info}POMs are updated. Committing and tagging...$r"

git commit -am"Version ${VERSION}"
git tag "v${VERSION}"
echo "${info}Pushing the v${VERSION} tag to origin.$r"
git push --tags origin

echo "${info}Deploying to CGP Artifact Registry"
mvnd clean deploy -P${PROFILES}

echo "${info}Updating POMs to version ${NEXTVERSION}.$r"
mvnd versions:set -DautoVersionSubmodules=true -P${PROFILES} -DnewVersion="${NEXTVERSION}-SNAPSHOT"
git commit -am"Version bump"
git push
echo "${info}POMs are updated.$r"

echo "${info}All done.$r"
echo "${instruction}Remember to:"
echo "   - mark $VERSION as released and archived $NEXTVERSION: https://antea.myjetbrains.com/youtrack/projects/VL?tab=fields"
