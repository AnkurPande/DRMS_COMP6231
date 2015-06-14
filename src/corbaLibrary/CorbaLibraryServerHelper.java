package corbaLibrary;

/** 
 * Helper class for : CorbaLibraryServer
 *  
 * @author OpenORB Compiler
 */ 
public class CorbaLibraryServerHelper
{
    /**
     * Insert CorbaLibraryServer into an any
     * @param a an any
     * @param t CorbaLibraryServer value
     */
    public static void insert(org.omg.CORBA.Any a, corbaLibrary.CorbaLibraryServer t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract CorbaLibraryServer from an any
     *
     * @param a an any
     * @return the extracted CorbaLibraryServer value
     */
    public static corbaLibrary.CorbaLibraryServer extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return corbaLibrary.CorbaLibraryServerHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the CorbaLibraryServer TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "CorbaLibraryServer" );
        }
        return _tc;
    }

    /**
     * Return the CorbaLibraryServer IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:corbaLibrary/CorbaLibraryServer:1.0";

    /**
     * Read CorbaLibraryServer from a marshalled stream
     * @param istream the input stream
     * @return the readed CorbaLibraryServer value
     */
    public static corbaLibrary.CorbaLibraryServer read(org.omg.CORBA.portable.InputStream istream)
    {
        return(corbaLibrary.CorbaLibraryServer)istream.read_Object(corbaLibrary._CorbaLibraryServerStub.class);
    }

    /**
     * Write CorbaLibraryServer into a marshalled stream
     * @param ostream the output stream
     * @param value CorbaLibraryServer value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, corbaLibrary.CorbaLibraryServer value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to CorbaLibraryServer
     * @param obj the CORBA Object
     * @return CorbaLibraryServer Object
     */
    public static CorbaLibraryServer narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof CorbaLibraryServer)
            return (CorbaLibraryServer)obj;

        if (obj._is_a(id()))
        {
            _CorbaLibraryServerStub stub = new _CorbaLibraryServerStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to CorbaLibraryServer
     * @param obj the CORBA Object
     * @return CorbaLibraryServer Object
     */
    public static CorbaLibraryServer unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof CorbaLibraryServer)
            return (CorbaLibraryServer)obj;

        _CorbaLibraryServerStub stub = new _CorbaLibraryServerStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
