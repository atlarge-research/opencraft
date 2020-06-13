#!/bin/sh

OS=$(uname -s)

# Run script within the directory
case "${OS}" in
	Darwin*)	BINDIR=$(dirname "$(greadlink -fn "$0")");;
	*)	BINDIR=$(dirname "$(readlink -fn "$0")")
esac
cd "$BINDIR"

# Build Opencraft
(cd .. && mvn -T 1C -B package)
