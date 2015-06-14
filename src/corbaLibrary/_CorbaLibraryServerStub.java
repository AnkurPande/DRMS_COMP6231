package corbaLibrary;

/**
 * Interface definition: CorbaLibraryServer.
 * 
 * @author OpenORB Compiler
 */
public class _CorbaLibraryServerStub extends org.omg.CORBA.portable.ObjectImpl
        implements CorbaLibraryServer
{
    static final String[] _ids_list =
    {
        "IDL:corbaLibrary/CorbaLibraryServer:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = corbaLibrary.CorbaLibraryServerOperations.class;

    /**
     * Operation createAccount
     */
    public boolean createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String eduInstitution)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createAccount",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(emailAddress);
                    _output.write_string(phoneNumber);
                    _output.write_string(username);
                    _output.write_string(password);
                    _output.write_string(eduInstitution);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createAccount",_opsClass);
                if (_so == null)
                   continue;
                corbaLibrary.CorbaLibraryServerOperations _self = (corbaLibrary.CorbaLibraryServerOperations) _so.servant;
                try
                {
                    return _self.createAccount( firstName,  lastName,  emailAddress,  phoneNumber,  username,  password,  eduInstitution);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation reserveBook
     */
    public boolean reserveBook(String username, String password, String bookName, String authorName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("reserveBook",true);
                    _output.write_string(username);
                    _output.write_string(password);
                    _output.write_string(bookName);
                    _output.write_string(authorName);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reserveBook",_opsClass);
                if (_so == null)
                   continue;
                corbaLibrary.CorbaLibraryServerOperations _self = (corbaLibrary.CorbaLibraryServerOperations) _so.servant;
                try
                {
                    return _self.reserveBook( username,  password,  bookName,  authorName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation reserveInterLibrary
     */
    public boolean reserveInterLibrary(String username, String password, String bookName, String authorName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("reserveInterLibrary",true);
                    _output.write_string(username);
                    _output.write_string(password);
                    _output.write_string(bookName);
                    _output.write_string(authorName);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reserveInterLibrary",_opsClass);
                if (_so == null)
                   continue;
                corbaLibrary.CorbaLibraryServerOperations _self = (corbaLibrary.CorbaLibraryServerOperations) _so.servant;
                try
                {
                    return _self.reserveInterLibrary( username,  password,  bookName,  authorName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getNonRetuners
     */
    public String getNonRetuners(String adminUsername, String adminPassword, String eduInstitution, String numDays)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getNonRetuners",true);
                    _output.write_string(adminUsername);
                    _output.write_string(adminPassword);
                    _output.write_string(eduInstitution);
                    _output.write_string(numDays);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNonRetuners",_opsClass);
                if (_so == null)
                   continue;
                corbaLibrary.CorbaLibraryServerOperations _self = (corbaLibrary.CorbaLibraryServerOperations) _so.servant;
                try
                {
                    return _self.getNonRetuners( adminUsername,  adminPassword,  eduInstitution,  numDays);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation setDuration
     */
    public boolean setDuration(String username, String bookName, int numDays)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("setDuration",true);
                    _output.write_string(username);
                    _output.write_string(bookName);
                    _output.write_long(numDays);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setDuration",_opsClass);
                if (_so == null)
                   continue;
                corbaLibrary.CorbaLibraryServerOperations _self = (corbaLibrary.CorbaLibraryServerOperations) _so.servant;
                try
                {
                    return _self.setDuration( username,  bookName,  numDays);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
