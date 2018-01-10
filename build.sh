#!/bin/bash
ROOT=`pwd`

git clone . target/kotlin-mvvm/
rm -rf kt-mvvm/
cd target/kotlin-mvvm/
git checkout master
git pull
mvn dokka:dokka

cd $ROOT
cp -r target/kotlin-mvvm/target/dokka/kt-mvvm/ kt-mvvm/
git add kt-mvvm/
git commit -m "Updating the Javadoc" kt-mvvm/

