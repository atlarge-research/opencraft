#!/bin/sh

OS=$(uname -s)

# Run script within the directory
case "${OS}" in
	Darwin*)	BINDIR=$(dirname "$(greadlink -fn "$0")");;
	*)	BINDIR=$(dirname "$(readlink -fn "$0")")
esac
cd "$BINDIR"

# Build Glowstone
(cd .. && mvn -s settings.xml -T 1C -B package)
