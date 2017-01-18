#!/bin/bash
JAR_SEPERATOR=":"

DEP_JARS="
./poi/poi-3.15/poi-scratchpad-3.15.jar
./poi/poi-3.15/poi-ooxml-3.15.jar
./poi/poi-3.15/lib/junit-4.12.jar
./poi/poi-3.15/lib/commons-logging-1.2.jar
./poi/poi-3.15/lib/commons-codec-1.10.jar
./poi/poi-3.15/lib/commons-collections4-4.1.jar
./poi/poi-3.15/lib/log4j-1.2.17.jar
./poi/poi-3.15/poi-excelant-3.15.jar
./poi/poi-3.15/ooxml-lib/curvesapi-1.04.jar
./poi/poi-3.15/ooxml-lib/xmlbeans-2.6.0.jar
./poi/poi-3.15/poi-ooxml-schemas-3.15.jar
./poi/poi-3.15/poi-examples-3.15.jar
./poi/poi-3.15/poi-3.15.jar
./libmysql/mysql-connector-java-5.1.39-bin.jar
"
for JAR in $DEP_JARS
do
    FINAL_DEP_JARS="${FINAL_DEP_JARS}${JAR_SEPERATOR}${JAR}"
done

cp */*.class .
cp */*/*.class .
java -cp ${FINAL_DEP_JARS}  Main
