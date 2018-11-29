package org.jumpmind.pos.devices.service.print;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Line.class, name = "line"), @Type(value = Barcode.class, name = "barcode"),
        @Type(value = FileImage.class, name = "image-file"), @Type(value = MemoryImage.class, name = "image-memory"),
        @Type(value = RuleLine.class, name = "ruleline"), @Type(value = CutPaper.class, name = "cutpaper"),
        @Type(value = BeginInsert.class, name = "begin-insert"), @Type(value = EndInsert.class, name = "end-insert"),
        @Type(value = BeginRemoval.class, name = "begin-removal"), @Type(value = EndRemoval.class, name = "end-removal") })
abstract public class DocumentElement {

}