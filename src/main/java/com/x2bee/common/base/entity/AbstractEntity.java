package com.x2bee.common.base.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

import com.x2bee.common.base.exception.CommonException;

public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = -3144023307496112743L;

    public String toBase64(){
        try(
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		ObjectOutputStream oos = new ObjectOutputStream(baos);
        ) {
        	oos.writeObject(this);
        	oos.flush();
        	return new String(Base64.encodeBase64String(baos.toByteArray())).replaceAll("\n", "").replaceAll("\r", "");
        } catch (IOException ioe) {
            throw new CommonException(ioe);
        }
    }

}
