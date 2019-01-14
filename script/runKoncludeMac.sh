#!/bin/bash
printf "Konclude started \n" ;
echo "waiting for its log ..." ;
app/KoncludeMac realization -i aux/onto.functionalsyntax.owl | sed $'s/[^[:print:]\t]//g' ;
echo "Konclude finished" ;
echo "****************************************************************" ;
