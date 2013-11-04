package org.openprovenance.prov.model;

public interface StatementOrBundle {
    
    public Kind getKind();
    
    public enum Kind {
	
	PROV_ENTITY,
	PROV_ACTIVITY,
	PROV_AGENT,
	PROV_USAGE,
	PROV_GENERATION,
	PROV_INVALIDATION,
	PROV_START,
	PROV_END,
	PROV_COMMUNICATION,
	PROV_DERIVATION,
	PROV_ASSOCIATION,
	PROV_ATTRIBUTION,
	PROV_DELEGATION,
	PROV_INFLUENCE,
	PROV_ALTERNATE,
	PROV_SPECIALIZATION,
	PROV_MENTION,
	PROV_MEMBERSHIP,
	PROV_BUNDLE,
	PROV_DICTIONARY_INSERTION,
	PROV_DICTIONARY_REMOVAL,
	PROV_DICTIONARY_MEMBERSHIP
    }

}
