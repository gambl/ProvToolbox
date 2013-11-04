package org.openprovenance.prov.notation;
import java.net.URI;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.model.InternationalizedString;
import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.ValueConverter;

/* Class to traverse the syntax tree generated by antl parser, invoking constructor methods in TreeConstructor interface. */

public class TreeTraversal {

    final private ModelConstructor c;
    final private ProvFactory pFactory;
    final private ValueConverter vconv;
    
    public TreeTraversal(ModelConstructor c, ProvFactory pFactory) {
        this.c=c;
        this.pFactory=pFactory;
        this.vconv=new ValueConverter(pFactory);
    }

    public String getTokenString(Tree t) {
        if (t==null) return null;
        if (((CommonTree)t).getToken()==null) return null;
        return ((CommonTree)t).getToken().getText();
    }

    public String convertToken(String token) {
        return token;
    }

    public int convertInt(String token) {
        return Integer.valueOf(token);
    }

    public String stripAmpersand(String token) {
        return token.substring(1);
    }

    public String unquoteTwice(String token) {
        return token.substring(2,token.length()-2);
    }
    public String unquoteTrice(String token) {
        return token.substring(3,token.length()-3);
    }

    public Object convert(Tree ast) {
        switch(ast.getType()) {

            /* Component 1 */

        case PROV_NParser.ENTITY:
            QName id=(QName)convert(ast.getChild(0));
            List<Attribute> eAttrs=(List<Attribute>) convert(ast.getChild(1));
            return c.newEntity(id,eAttrs);

        case PROV_NParser.ACTIVITY:
            id=(QName)convert(ast.getChild(0));
            XMLGregorianCalendar startTime=(XMLGregorianCalendar)convert(ast.getChild(1));
            XMLGregorianCalendar endTime=(XMLGregorianCalendar)convert(ast.getChild(2));
            List<Attribute> aAttrs=(List<Attribute>) convert(ast.getChild(3));
            return c.newActivity(id,startTime,endTime,aAttrs);

        case PROV_NParser.START:
        case PROV_NParser.END:
            if (ast.getChildCount()==0) return null;
            if (ast.getChild(0)==null) return null;
	    return convert (ast.getChild(0)); 

        case PROV_NParser.USED:
            Tree uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            QName uid=(QName) convert(uidTree);
            QName id2=(QName) convert(ast.getChild(1));
            QName id1=(QName) convert(ast.getChild(2));
            XMLGregorianCalendar time=(XMLGregorianCalendar) convert(ast.getChild(3));
            List<Attribute> rAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newUsed(uid, id2,id1,time,rAttrs);

        case PROV_NParser.WGB:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName) convert(ast.getChild(1));
            id1=(QName) convert(ast.getChild(2));
            time=(XMLGregorianCalendar) convert(ast.getChild(3));
            rAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newWasGeneratedBy(uid,id2,id1,time,rAttrs);

        case PROV_NParser.WSB:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            QName id3=(QName)convert(ast.getChild(3));
            time=(XMLGregorianCalendar) convert(ast.getChild(4));
            rAttrs=(List<Attribute>) convert(ast.getChild(5));
            return c.newWasStartedBy(uid,id2,id1,id3,time,rAttrs);


        case PROV_NParser.WEB:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            id3=(QName) convert(ast.getChild(3));
            time=(XMLGregorianCalendar) convert(ast.getChild(4));
            rAttrs=(List<Attribute>) convert(ast.getChild(5));
            return c.newWasEndedBy(uid,id2,id1,id3,time,rAttrs);

        case PROV_NParser.TIME:
            if (ast.getChildCount()==0) return null;
            if (ast.getChild(0)==null) return null;
            return pFactory.newISOTime(getTokenString(ast.getChild(0)));

        case PROV_NParser.WINVB:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            time=(XMLGregorianCalendar) convert(ast.getChild(3));
            rAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newWasInvalidatedBy(uid,id2,id1,time,rAttrs);



        case PROV_NParser.WIB:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            rAttrs=(List<Attribute>) convert(ast.getChild(3));
            return c.newWasInformedBy(uid,id2,id1,rAttrs);


            /* Component 2 */

        case PROV_NParser.AGENT:
            id=(QName) convert(ast.getChild(0));
            List<Attribute> agAttrs=(List<Attribute>) convert(ast.getChild(1));
            return c.newAgent(id,agAttrs);


        case PROV_NParser.WAT:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            rAttrs=(List<Attribute>) convert(ast.getChild(3));
            return c.newWasAttributedTo(uid,id2,id1,rAttrs);


        case PROV_NParser.WAW:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName) ((ast.getChild(2)==null)?null : convert(ast.getChild(2)));
            QName pl=(QName) ((ast.getChild(3)==null)?null : convert(ast.getChild(3)));
            rAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newWasAssociatedWith(uid,id2,id1,pl,rAttrs);


        case PROV_NParser.AOBO:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            QName a=(QName) ((ast.getChild(3)==null)?null : convert(ast.getChild(3)));
            rAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newActedOnBehalfOf(uid,id2,id1,a,rAttrs);


            /* Component 3 */

        case PROV_NParser.WDF:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            QName pe=(QName) convert(ast.getChild(3));
            QName q2=(QName) convert(ast.getChild(4));
            QName q1=(QName) convert(ast.getChild(5));
            List<Attribute> dAttrs=(List<Attribute>) convert(ast.getChild(6));
            return c.newWasDerivedFrom(uid,id2,id1,pe,q2,q1,dAttrs);

            /*
        case PROV_NParser.WRO:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            pe=convert(ast.getChild(3));
            q2=convert(ast.getChild(4));
            q1=convert(ast.getChild(5));
            dAttrs=convert(ast.getChild(6));
            return c.newWasRevisionOf(uid,id2,id1,pe,q2,q1,dAttrs);

        case PROV_NParser.WQF:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            pe=convert(ast.getChild(3));
            q2=convert(ast.getChild(4));
            q1=convert(ast.getChild(5));
            dAttrs=convert(ast.getChild(6));
            return c.newWasQuotedFrom(uid,id2,id1,pe,q2,q1,dAttrs);

        case PROV_NParser.PRIMARYSOURCE:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            pe=convert(ast.getChild(3));
            q2=convert(ast.getChild(4));
            q1=convert(ast.getChild(5));
            dAttrs=convert(ast.getChild(6));
            return c.newHadPrimarySource(uid,id2,id1,pe,q2,q1,dAttrs);
*/
            
        case PROV_NParser.INFL:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            dAttrs=(List<Attribute>) convert(ast.getChild(3));
            return c.newWasInfluencedBy(uid,id2,id1,dAttrs);

            /* Component 4 */

            /* Component 5 */

        case PROV_NParser.ALTERNATE:
            id2=(QName)convert(ast.getChild(0));
            id1=(QName)convert(ast.getChild(1));
            return c.newAlternateOf(id2,id1);

        case PROV_NParser.SPECIALIZATION:
            id2=(QName)convert(ast.getChild(0));
            id1=(QName)convert(ast.getChild(1));
            return c.newSpecializationOf(id2,id1);

        case PROV_NParser.MEM:
            id2=(QName)convert(ast.getChild(0));
            id1=(QName)convert(ast.getChild(1));
            List<QName> ll=new LinkedList<QName>();
            if (id1!=null) ll.add(id1);
            return c.newHadMember(id2,ll);

        case PROV_NParser.CTX:
            QName su=(QName)convert(ast.getChild(0));
            QName bu=(QName)convert(ast.getChild(1));
            QName ta=(QName)convert(ast.getChild(2));
            return c.newMentionOf(su,bu,ta);



            /* Component 6 */

        case PROV_NParser.DBIF:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            List<KeyQNamePair> keymap=(List<KeyQNamePair>)convert(ast.getChild(3));
            dAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newDerivedByInsertionFrom(uid,id2,id1,keymap,dAttrs);

        case PROV_NParser.DBRF:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            id1=(QName)convert(ast.getChild(2));
            Object keyset=convert(ast.getChild(3));
            dAttrs=(List<Attribute>)convert(ast.getChild(4));
            return c.newDerivedByRemovalFrom(uid,id2,id1,(List<Object>)keyset,dAttrs);

        case PROV_NParser.DMEM:
            //uidTree=ast.getChild(0);
            //if (uidTree.getChildCount()>0) {
            //    uidTree=uidTree.getChild(0);
            //}
            //uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            keymap=(List<KeyQNamePair>) convert(ast.getChild(2));
            //dAttrs=(List<Attribute>) convert(ast.getChild(4));
            return c.newDictionaryMembership(id2,keymap);

        case PROV_NParser.CMEM:
            uidTree=ast.getChild(0);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);
            id2=(QName)convert(ast.getChild(1));
            Object cmemEntities=convert(ast.getChild(2));
            //complete=convert(ast.getChild(3));
            dAttrs=(List<Attribute>) convert(ast.getChild(4));
            //return c.newCollectionMemberOf(uid,id2,cmemEntities,complete,dAttrs);
            return null;


        case PROV_NParser.KEYS:
            List<Object> keys=new LinkedList<Object>();
            for (int i=0; i< ast.getChildCount(); i++) {
                Object o=convert(ast.getChild(i));
                keys.add(o);
            }
            return keys;


        case PROV_NParser.VALUES:
            List<Object> values=new LinkedList<Object>();
            for (int i=0; i< ast.getChildCount(); i++) {
                Object o=convert(ast.getChild(i));
                values.add(o);
            }
            return values;

        case PROV_NParser.KES:
            Object keys1=convert(ast.getChild(0));
            Object entities=convert(ast.getChild(1));

            @SuppressWarnings("unchecked")
            List<TypedValue> keys2 = (List<TypedValue>)keys1;
	    
            @SuppressWarnings("unchecked")
            List<QName> qnames = (List<QName>)entities;
	    
            List<KeyQNamePair> entries=new LinkedList<KeyQNamePair>();
            int ii=0;
            for (TypedValue key : keys2) {
                QName value=qnames.get(ii);
                KeyQNamePair p=new KeyQNamePair();
                p.name=value;
                p.key=key;
               entries.add(p);
                ii++;
            }

            return entries;

        case PROV_NParser.ES:
            List<Object> listOfEntities=new LinkedList<Object>();
            for (int i=0; i< ast.getChildCount(); i++) {
                Object o=convert(ast.getChild(i));
                listOfEntities.add(o);
            }
            return listOfEntities;

            /* Component 6 */

        case PROV_NParser.EXT:
        	System.out.println("FOUND Extension " + ast);
            Object extName=convert(ast.getChild(0));
            uidTree=ast.getChild(1);
            if (uidTree.getChildCount()>0) {
                uidTree=uidTree.getChild(0);
            }
            uid=(QName)convert(uidTree);

            Object optionalAttributes=convert(ast.getChild(ast.getChildCount()-1));
            List<Object> args=new LinkedList<Object>();
            for (int i=2; i< ast.getChildCount()-1; i++) {
                Object o=convert(ast.getChild(i));
                args.add(o);
            }
            //return c.newExtension(extName, uid, args, optionalAttributes);
            return null;

            /* Miscellaneous Constructs */

        case PROV_NParser.EXPRESSIONS:
        case PROV_NParser.BUNDLES:
            List<Object> records=new LinkedList<Object>();
            for (int i=0; i< ast.getChildCount(); i++) {
                Object o=convert(ast.getChild(i));
                records.add(o);
            }
            return records;

        case PROV_NParser.DOCUMENT:
            Hashtable<String,String> nss=(Hashtable<String,String>)convert(ast.getChild(0));
            pFactory.getNss().putAll(nss);
            //c.startBundle(null);
            //System.out.println("Document (UnNamed bunded) ");
            @SuppressWarnings("unchecked")
            List<Statement> records2=(List<Statement>)convert(ast.getChild(1));
            List<NamedBundle> bundles=null;
            if (ast.getChild(2)!=null) {
	        @SuppressWarnings("unchecked")
                List<NamedBundle> tmp = (List<NamedBundle>)convert(ast.getChild(2));
	        bundles=tmp;
            }
            return c.newDocument(nss,records2, bundles);

        case PROV_NParser.BUNDLE:
            namespaceTable=new Hashtable<String, String>();

            Hashtable<String,String> nss2=(Hashtable<String,String>)convert(ast.getChild(1));
            
            // parse bundleId after namespace declarations
            QName bundleId=(QName) convert(ast.getChild(0));

            //c.startBundle(bundleId);

            @SuppressWarnings("unchecked")
            List<Statement> records3=(List<Statement>)convert(ast.getChild(2));
            return c.newNamedBundle(bundleId,nss2,records3);
            
        case PROV_NParser.ATTRIBUTES:
            List<Attribute> attributes=new LinkedList<Attribute>();
            for (int i=0; i< ast.getChildCount(); i++) {
                attributes.add((Attribute)convert(ast.getChild(i)));
            }
            return attributes;

        case PROV_NParser.ID:
            return pFactory.stringToQName(convertToken(getTokenString(ast.getChild(0))));

        case PROV_NParser.ATTRIBUTE:
            String attr1=convertToken(getTokenString(ast.getChild(0)));
            Object val1=convert(ast.getChild(1));
            
            if (val1 instanceof Object[]) {
            	Object [] values2=(Object[])val1;
            	Object theValue=values2[0];
            	QName theType=(QName)values2[1];
		    
            	//return pFactory.newAttribute(pFactory.stringToQName(attr1),val1,vconv.getXsdType(val1));
            	if (ValueConverter.QNAME_XSD_QNAME.equals(theType)) {
                	return pFactory.newAttribute(pFactory.stringToQName(attr1),pFactory.stringToQName((String)theValue),theType);            		
            	} else {
                	return pFactory.newAttribute(pFactory.stringToQName(attr1),theValue,theType);
            
            	}
            } else if (val1 instanceof InternationalizedString) {
            	return pFactory.newAttribute(pFactory.stringToQName(attr1),val1,ValueConverter.QNAME_XSD_STRING);	
            } else { // TODO what case is it?
                return pFactory.newAttribute(pFactory.stringToQName(attr1),val1,ValueConverter.QNAME_XSD_STRING);	            	
            }

        case PROV_NParser.STRING:
            if (ast.getChildCount()==1) {
                return unwrap(convertToken(getTokenString(ast.getChild(0))));
            } else {
                return pFactory.newInternationalizedString(unwrap(convertToken(getTokenString(ast.getChild(0)))),
                                                           stripAmpersand(convertToken(getTokenString(ast.getChild(1)))));
            }
        case PROV_NParser.STRING_LONG:
            if (ast.getChildCount()==1) {
                return unquoteTrice(convertToken(getTokenString(ast.getChild(0))));
            } else {
                return pFactory.newInternationalizedString(unquoteTrice(convertToken(getTokenString(ast.getChild(0)))),
                                                           stripAmpersand(convertToken(getTokenString(ast.getChild(1)))));
            }

        case PROV_NParser.INT:
            return convertInt(getTokenString(ast.getChild(0)));
        
        case PROV_NParser.QNAM:
            return pFactory.stringToQName(convertToken(getTokenString(ast.getChild(0))));

        case PROV_NParser.IRI:
            String iri=convertToken(getTokenString(ast.getChild(0)));
            return URI.create(unwrap(iri));

        case PROV_NParser.TYPEDLITERAL:
            String v1=convertToken(getTokenString(ast.getChild(0)));
            QName v2;
            
            if (ast.getChild(1)==null) {
                v2=ValueConverter.QNAME_XSD_QNAME;
                //v1="\"" + v1 + "\"";
                return convertTypedLiteral(v2,v1);
            } else {
                v2=(QName)convert(ast.getChild(1));
                if (ast.getChild(2)!=null) {
                    Object iv1=pFactory.newInternationalizedString(unwrap(v1),
                                                                   stripAmpersand(convertToken(getTokenString(ast.getChild(2)))));
                    return convertTypedLiteral(v2,iv1);
                } else {
                    v1=unwrap(v1);
                    return convertTypedLiteral(v2,v1);
                }
            }

        case PROV_NParser.NAMESPACE:
            String pre=(String)convert(ast.getChild(0));
            String iri1=unwrap(getTokenString(ast.getChild(1)));
            if (pre!=null) // should not occur
                namespaceTable.put(pre,iri1);
            return null;

        case PROV_NParser.DEFAULTNAMESPACE:
            iri1=unwrap(getTokenString(ast.getChild(0)));
            namespaceTable.put("_",iri1);
            return null;

        case PROV_NParser.PREFIX:
            String prefix=getTokenString(ast.getChild(0));
            return prefix;

        case PROV_NParser.NAMESPACES:
            for (int i=0; i< ast.getChildCount(); i++) {
                Object o=convert(ast.getChild(i));            
            }
            return namespaceTable;


        case PROV_NParser.TRUE:
            return true;
        case PROV_NParser.FALSE:
            return false;
        case PROV_NParser.UNKNOWN:
            return null;

        }


        return null;

    }
    
    Hashtable<String,String> namespaceTable=new Hashtable<String, String>();

    public Object convertTypedLiteral(QName datatype, Object value) {
    	Object [] valueTypePair=new Object[] {value,datatype};
    	return valueTypePair;
    /*	
        if (value instanceof String) {
            Object val=vconv.convertToJava(datatype,(String)value);
            return val;
        } else {
            return value;
        }
        */
    }



    public String unwrap (String s) {
        return s.substring(1,s.length()-1);
    }
   

}

