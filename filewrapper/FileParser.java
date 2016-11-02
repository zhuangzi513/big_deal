package filewrapper;

import common.DetailDealElement;

import java.io.IOException;
import java.util.*; 

abstract class FileParser {
    abstract boolean parseFileIntoDealElements() throws IOException;
    abstract Vector<DetailDealElement> getDetailDealElements();
};
