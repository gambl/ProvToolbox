package org.openprovenance.prov.rdf;
import org.openprovenance.prov.notation.TreeConstructor;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.net.URI;
import javax.xml.datatype.XMLGregorianCalendar;

public class RdfConstructor implements TreeConstructor {
    final ProvFactory pFactory;
    final ElmoManager manager;

    Hashtable<String,String>  namespaceTable = new Hashtable<String,String>();

    public RdfConstructor(ProvFactory pFactory, ElmoManager manager) {
        this.pFactory=pFactory;
        this.manager=manager;
        pFactory.setNamespaces(namespaceTable);
    }

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        QName qname = getQName(id);
        Activity a = (Activity) manager.designate(qname, Activity.class);
        return a;
    }

    public Object convertEntity(Object id, Object attrs) {
        QName qname = getQName(id);
        Entity e;
        if (qname.getLocalPart().equals("rec-advance")) {
            //Hack, I need to parse the attributes
            e = (Entity) manager.designate(qname, Plan.class);
        } else {
            e = (Entity) manager.designate(qname, Entity.class);
        }
        return e;
    }

    public Object convertAgent(Object id, Object attrs) {
        QName qname = getQName(id);
        Agent ag = (Agent) manager.designate(qname, Agent.class);
        return ag;
    }

    public Object convertBundle(Object nss, List<Object> records, List<Object> bundles) {
        return null;
    }
    public Object convertNamedBundle(Object id, Object nss, List<Object> records) {
        return null;
    }
    public Object convertAttributes(List<Object> attributes) {
        return null;
    }
    public Object convertId(String id) {
        return id;
    }
    public Object convertAttribute(Object name, Object value) {
        return null;
    }
    public Object convertStart(String start) {
        return null;
    }
    public Object convertEnd(String end) {
        return null;
    }

    public Object convertString(String s) {
        s=unwrap(s);
        return s;
    }

    public Object convertString(String s, String lang) {
        s=unwrap(s);
	return s + "@" + lang;
    }



    public Object convertInt(int s) {
        return null;
    }


    public <INFLUENCE,TYPE> INFLUENCE addEntityInfluence(Object id,
							 TYPE e2,
							 Entity e1,
							 Object time, 
							 Object aAttrs,
							 Class<INFLUENCE> cl) {
	
        INFLUENCE infl=null;

        if ((id!=null)  || (time!=null) || (aAttrs!=null)) {
	    QName qname = getQName(id);
	    infl = manager.designate(qname, cl);
            EntityInfluence qi=(EntityInfluence) infl;
            qi.getEntities().add(e1);
            addQualifiedInfluence(e2,infl);  

	    if (time!=null) {
		String s=(String)time;
		XMLGregorianCalendar t=pFactory.newISOTime(s);
		((InstantaneousEvent)infl).getAtTime().add(t);
	    }
        }
        return infl;
    }

    public <INFLUENCE,TYPE> INFLUENCE addActivityInfluence(Object id,
							   TYPE e2,
							   Activity a1,
							   Object time, 
							   Object aAttrs,
							   Class<INFLUENCE> cl) {
	
        INFLUENCE infl=null;

        if ((id!=null)  || (time!=null) || (aAttrs!=null)) {
	    QName qname = getQName(id);
	    infl = manager.designate(qname, cl);
            ActivityInfluence qi=(ActivityInfluence) infl;
            qi.getActivities().add(a1);
            addQualifiedInfluence(e2,infl);  

	    if (time!=null) {
		String s=(String)time;
		XMLGregorianCalendar t=pFactory.newISOTime(s);
		((InstantaneousEvent)infl).getAtTime().add(t);
	    }
        }
        return infl;
    }

    public <INFLUENCE,TYPE> INFLUENCE addAgentInfluence(Object id,
							   TYPE e2,
							   Agent a1,
							   Object time, 
							   Object aAttrs,
							   Class<INFLUENCE> cl) {
	
        INFLUENCE infl=null;

        if ((id!=null)  || (time!=null) || (aAttrs!=null)) {
	    QName qname = getQName(id);
	    infl = manager.designate(qname, cl);
            AgentInfluence qi=(AgentInfluence) infl;
            qi.getAgents().add(a1);
            addQualifiedInfluence(e2,infl);  

	    if (time!=null) {
		String s=(String)time;
		XMLGregorianCalendar t=pFactory.newISOTime(s);
		((InstantaneousEvent)infl).getAtTime().add(t);
	    }
        }
        return infl;
    }

    public Object convertUsed(Object id, Object id2, Object id1, Object time, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);

        Entity e1=(Entity)manager.find(qn1);
        Activity a2=(Activity)manager.find(qn2);
        Usage u=addEntityInfluence(id, a2, e1, time, aAttrs,Usage.class);

	a2.getUsed().add(e1);

        return u;
    }


    public <INFLUENCE,TYPE1> INFLUENCE test(INFLUENCE foo, TYPE1 foo1) {
	return foo;
    }


    // not pretty

    public <INFLUENCE,EFFECT> void addQualifiedInfluence(EFFECT e2, INFLUENCE g) {
	if (g!=null) {
	    if (g instanceof Generation) {
		((Entity)e2).getQualifiedGeneration().add((Generation)g);
	    } else if  (g instanceof Invalidation) {
		((Entity)e2).getQualifiedInvalidation().add((Invalidation)g);
	    } else if  (g instanceof Communication) {
		((Activity)e2).getQualifiedCommunication().add((Communication)g);
	    } else if  (g instanceof Usage) {
		((Activity)e2).getQualifiedUsage().add((Usage)g);
	    } else if  (g instanceof Start) {
		((Activity)e2).getQualifiedStart().add((Start)g);
	    } else if  (g instanceof End) {
		((Activity)e2).getQualifiedEnd().add((End)g);
	    } else {
		throw new UnsupportedOperationException();
	    }
	}
    }



    public Object convertWasGeneratedBy(Object id, Object id2, Object id1, Object time, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);

        Entity e2=(Entity)manager.find(qn2);
        Activity a1=(Activity)manager.find(qn1);

        Generation g=addActivityInfluence(id,e2, a1, time,aAttrs,Generation.class);

        e2.getWasGeneratedBy().add(a1);
        return g;
    }

    public Object convertWasStartedBy(Object id, Object id2, Object id1, Object id3,
				      Object time, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        QName qn3 = getQName(id3);

        Entity e1=(Entity)manager.find(qn1);
        Activity a2=(Activity)manager.find(qn2);
        Start s=addEntityInfluence(id, a2, e1, time, aAttrs, Start.class);

	if (qn3!=null) {
	    Activity a3=(Activity)manager.find(qn3);
	    s.getHadActivity().add(a3);
	}

	a2.getWasStartedBy().add(e1);

        return s;
    }

    public Object convertWasEndedBy(Object id, Object id2, Object id1, Object id3,
				      Object time, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        QName qn3 = getQName(id3);

        Entity e1=(Entity)manager.find(qn1);
        Activity a2=(Activity)manager.find(qn2);
        End s=addEntityInfluence(id, a2, e1, time, aAttrs, End.class);

	if (qn3!=null) {
	    Activity a3=(Activity)manager.find(qn3);
	    s.getHadActivity().add(a3);
	}

	a2.getWasEndedBy().add(e1);

        return s;
    }

    public Object convertWasInvalidatedBy(Object id, Object id2, Object id1, Object time, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);

        Entity e2=(Entity)manager.find(qn2);
        Activity a1=(Activity)manager.find(qn1);

        Invalidation g=addActivityInfluence(id,e2, a1, time,aAttrs,Invalidation.class);



        e2.getWasInvalidatedBy().add(a1);
        return g;
    }

    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);

        Activity e2=(Activity)manager.find(qn2);
        Activity a1=(Activity)manager.find(qn1);

        Communication g=addActivityInfluence(id,e2, a1, null, aAttrs,Communication.class);

        e2.getWasInformedBy().add(a1);
        return g;
    }



    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object gAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        Entity e2=(Entity)manager.find(qn2);
        Entity e1=(Entity)manager.find(qn1);

        e2.getWasDerivedFrom().add(e1);

        
        return null;
    }

    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertHadPrimarySource(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertWasInfluencedBy(Object id, Object id2, Object id1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertAlternateOf(Object id2, Object id1) {
        return null;
    }

    public Object convertSpecializationOf(Object id2,Object id1) {
        return null;
    }
    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        return null;
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        QName qnpl = getQName(pl);

        Association a = (Association) manager.designate(qname, Association.class);
        AgentInfluence qi=(AgentInfluence) a;

        Activity a2=(Activity)manager.find(qn2);
        Agent ag1=(Agent)manager.find(qn1);
        qi.getAgents().add(ag1);

        a2.getQualifiedAssociation().add(a);

	if (qnpl!=null) {
	    Plan plan=(Plan)manager.find(qnpl);
	    a.getHadPlan().add(plan);
	}

	a2.getWasAssociatedWith().add(ag1);

        return a;

    }

    public Object convertExtension(Object name, Object id, Object args, Object dAttrs) {
	return null;
    }
    public Object convertQualifiedName(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        iri=unwrap(iri);
        return URI.create(iri);
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        return null;
    }
    public Object convertNamespace(Object pre, Object iri) {
	String s_pre=(String)pre;
	String s_iri=(String)iri;
	s_iri=unwrap(s_iri);
	namespaceTable.put(s_pre,s_iri);
	return null;
    }

    public Object convertDefaultNamespace(Object iri) {
	String s_iri=(String)iri;
	s_iri=unwrap(s_iri);
	namespaceTable.put("_",s_iri);
	return null;
    }
    public Object convertNamespaces(List<Object> namespaces) {
        pFactory.setNamespaces(namespaceTable);
        return namespaceTable;
    }
    public Object convertPrefix(String pre) {
        return pre;
    }

    
    public QName getQName(Object id) {
        String idAsString=(String)id;
        return pFactory.stringToQName(idAsString);
    }

    public String unwrap (String s) {
        return s.substring(1,s.length()-1);
    }


    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertEntry(Object o1, Object o2) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeyEntitySet(List<Object> o) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertRemoval(Object id, Object id2, Object id1, Object keys, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertDictionaryMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertCollectionMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeys(List<Object> keys) {
        //todo
        throw new UnsupportedOperationException();
    }

    /* Component 6 */



    public Object convertMentionOf(Object su, Object bu, Object ta) {
        //todo
        throw new UnsupportedOperationException();
    }

        
}
