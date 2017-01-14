function isTarget(packagename)
        return packagename == 'edu.utd.s3.cszuo.methodmonitor'
end

function bA(param, pending)
        xpb:print(param.thisObject)
        xpb:print(param)
        xpb:print('bbb')
        pending:append('sssasas')
end

function handleLoadPackage(loadPackageParam)
        --xpb:print('aaa')
        --xpb:print(loadPackageParam.packageName)
        --xpb:print('aaa')
        cc = xpb:newHookMethodInstance()
        cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.MethodMonitor','onCreate',{'android.os.Bundle'})
        cc:setLuaBeforehook(bA)

        cc = xpb:newHookConstructorInstance()
        cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.lua.Xpb',{})
        cc:setLuaBeforehook(bA)


        cc = xpb:newHookConstructorInstance()
        cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.lua.Xpb',{})
        cc:setLuaBeforehook(bA)
end