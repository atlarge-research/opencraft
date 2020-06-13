#!/usr/bin/env bash

#http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

# Create builds dir if it's not already there.
mkdir -p builds

# Create a unique filename
DATE=$(date +"%Y%m%d")
for i in {1..1000}; do
	FILE=builds/${DATE}-$i
	if [ ! -f "${FILE}.jar" ] && [ ! -f "${FILE}.zip" ]; then
		break
	fi
	if [ $i -eq "1000" ]; then
		echo "Congratulations, you've compiled Opencraft 1000 times today!"
		echo "Unfortunately, the build script is not prepared for this and will exit now."
		exit 1
	fi
done

./scripts/build.sh
cp target/opencraft.jar ${FILE}.jar
zip -r ${FILE}.zip . --exclude builds .DS_Store
