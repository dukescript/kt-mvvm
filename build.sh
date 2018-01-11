#!/bin/bash
ROOT=`pwd`
REPO=https://github.com/dukescript/kt-mvvm.git

set -e

rm -rf target/
git clone . target/clone/
rm -rf kt-mvvm/
cd target/clone/
git fetch $REPO master
git checkout -f FETCH_HEAD -B master
mvn dokka:dokka

cd $ROOT
cp -r target/clone/target/dokka/kt-mvvm/ kt-mvvm/

echo ##### Status #####
git status
echo ##### Diff #####
git diff
echo ##### Integrating #####

if git diff-index --quiet HEAD --; then
  echo No changes!
else
  git add kt-mvvm/
  git commit -m "Updating the Javadoc" kt-mvvm/
  git log -n1
fi
