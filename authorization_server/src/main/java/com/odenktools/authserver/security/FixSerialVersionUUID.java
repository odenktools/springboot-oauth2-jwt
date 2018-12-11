package com.odenktools.authserver.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

class FixSerialVersionUUID extends ObjectInputStream {

	FixSerialVersionUUID(byte[] bytes) throws IOException {

		super(new ByteArrayInputStream(bytes));
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {

		ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

		if (resultClassDescriptor.getName().equals(SimpleGrantedAuthority.class.getName())) {
			ObjectStreamClass mostRecentSerialVersionUUID = ObjectStreamClass.lookup(SimpleGrantedAuthority.class);
			return mostRecentSerialVersionUUID;
		}

		return resultClassDescriptor;
	}
}
