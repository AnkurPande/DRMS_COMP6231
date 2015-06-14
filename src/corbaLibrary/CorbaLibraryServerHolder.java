package corbaLibrary;

/**
 * Holder class for : CorbaLibraryServer
 * 
 * @author OpenORB Compiler
 */
final public class CorbaLibraryServerHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal CorbaLibraryServer value
     */
    public corbaLibrary.CorbaLibraryServer value;

    /**
     * Default constructor
     */
    public CorbaLibraryServerHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public CorbaLibraryServerHolder(corbaLibrary.CorbaLibraryServer initial)
    {
        value = initial;
    }

    /**
     * Read CorbaLibraryServer from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = CorbaLibraryServerHelper.read(istream);
    }

    /**
     * Write CorbaLibraryServer into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        CorbaLibraryServerHelper.write(ostream,value);
    }

    /**
     * Return the CorbaLibraryServer TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return CorbaLibraryServerHelper.type();
    }

}
