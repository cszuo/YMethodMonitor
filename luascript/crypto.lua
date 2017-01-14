function isTarget(packagename)
        return true
end

function bA(param, pending)
        pending:append('crypto ')
        --pending:append(param:getResult():getClass():toString())
end

function handleLoadPackage(loadPackageParam)
        --cc = xpb:newHookMethodInstance()
        --cc:hook(loadPackageParam,'java.security.MessageDigest','getInstance',{'java.lang.String'})
        --cc:setLuaBeforehook(bA)

        --cc = xpb:newHookMethodInstance()
        --cc:hook(loadPackageParam,'java.security.MessageDigest','digest',{'[B'})
        --cc:setLuaBeforehook(bA)

        cc = xpb:newHookMethodInstance()
        cc:hook(loadPackageParam,'java.security.KeyStore','getInstance',{'java.lang.String'})
        cc:setLuaAfterhook(bA)

        cc = xpb:newHookMethodInstance()
        cc:hook(loadPackageParam,'java.security.KeyStore','load',{'java.io.InputStream','[C'})
        cc:setLuaBeforehook(function(param, pending)
                pending:append('cryptos ')
                jframe = luajava.bindClass("java.lang.String")
                frame = luajava.newInstance("java.lang.String", param.args[2]);
                pending:append(frame)
        end)

end