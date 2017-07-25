--
-- Created by IntelliJ IDEA.
-- User: cszuo
-- Date: 4/20/17
-- Time: 5:43 PM
-- To change this template use File | Settings | File Templates.
--

function isTarget(packagename)
    return packagename == 'com.petcube.android'
end

function bA(param, pending)
    --xpb:print(param.thisObject)
    --xpb:print(param)
    --xpb:print('bbb')
    pending:append('sssasas')
end

function handleLoadPackage(loadPackageParam)
    --xpb:print('aaa')
    --xpb:print(loadPackageParam.packageName)
    --xpb:print('aaa')
    cc = xpb:newHookMethodInstance()
    cc:hook(loadPackageParam,'android.bluetooth.BluetoothAdapter','getDefaultAdapter',{})
    cc:setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance()
    cc:hook(loadPackageParam,'android.bluetooth.BluetoothDevice','createRfcommSocketToServiceRecord',{'java.util.UUID'})
    cc:setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance()
    cc:hook(loadPackageParam,'android.bluetooth.BluetoothDevice','createInsecureRfcommSocketToServiceRecord',{'java.util.UUID'})
    cc:setLuaBeforehook(bA)


    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothDevice','connectGatt'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothAdapter','startLeScan'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getValue'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getDescriptor'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getIntValue'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getFloatValue'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getStringValue'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getProperties'):setLuaBeforehook(bA)
    cc = xpb:newHookMethodInstance():hook(loadPackageParam,'android.bluetooth.BluetoothGattCharacteristic','getInstanceId'):setLuaBeforehook(bA)



    --cc = xpb:newHookConstructorInstance()
    --cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.lua.Xpb',{})
    --cc:setLuaBeforehook(bA)


    --cc = xpb:newHookConstructorInstance()
    --cc:hook(loadPackageParam,'edu.utd.s3.cszuo.methodmonitor.lua.Xpb',{})
    --cc:setLuaBeforehook(bA)
end
