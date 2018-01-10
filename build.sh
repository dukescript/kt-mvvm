#!/bin/bash
ROOT=`pwd`
REPO=https://github.com/dukescript/kotlin-mvvm.git

set -e

rm -rf target/
git clone . target/kotlin-mvvm/
rm -rf kt-mvvm/
cd target/kotlin-mvvm/
git fetch $REPO master
git checkout -f FETCH_HEAD -B master
mvn dokka:dokka

cd $ROOT
cp -r target/kotlin-mvvm/target/dokka/kt-mvvm/ kt-mvvm/

echo ##### Status #####
pwd
git status
echo ##### Diff #####
git diff
echo ##### Integrating #####

git add kt-mvvm/
git commit -m "Updating the Javadoc" kt-mvvm/
git log -n1

