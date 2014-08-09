#!/bin/bash
chmod +x gradlew
./gradlew build
mkdir packaging
cp build/libs/* packaging/
cp -rf eclipse/assets/mods/* packaging/
mkdir out
cd packaging
mkdir mods
mv *.jar mods 
rm -rf "1.7.10"
cd ../libs && cp *.jar ../packaging/mods && cd ../packaging
tar czf ../out/zombie-total-conversion.tar.gz .