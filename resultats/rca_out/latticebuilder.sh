#!/bin/bash

for i in {0..1};
do
    dot -Tsvg step$i.dot -o step$i.svg;
    sed -i'' 's/(\(Concept_.*\))/(\<a xlink:href=\"#\1\"\>\1\<\/a\>)/' step$i.svg;
    sed -i'' 's/>\(Concept_.*_[0-9]*\)<\/text>/ id="\1">\1<\/text>/' step$i.svg;
    for j in {0..0};
    do
	   dot -Tpdf step$i-$j.dot -o step$i-$j.pdf;
    done
done