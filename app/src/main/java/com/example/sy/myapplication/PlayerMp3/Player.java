// **********************************************************************
//
// Copyright (c) 2003-2017 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.7.0
//
// <auto-generated>
//
// Generated from file `PlayerMp3.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.example.sy.myapplication.PlayerMp3;

public interface Player extends com.zeroc.Ice.Object
{
    boolean addSon(String name, String singer, String author, String album, String path, com.zeroc.Ice.Current current);

    boolean deleteSon(String name, com.zeroc.Ice.Current current);

    Son searchSon(String name, com.zeroc.Ice.Current current);

    Son[] getSons(com.zeroc.Ice.Current current);

    void MafactoryMethode(String[] s, com.zeroc.Ice.Current current);

    boolean play(String name, com.zeroc.Ice.Current current);

    void stop(com.zeroc.Ice.Current current);

    void pause(com.zeroc.Ice.Current current);

    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::PlayerMp3::Player"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::PlayerMp3::Player";
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addSon(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_name;
        String iceP_singer;
        String iceP_author;
        String iceP_album;
        String iceP_path;
        iceP_name = istr.readString();
        iceP_singer = istr.readString();
        iceP_author = istr.readString();
        iceP_album = istr.readString();
        iceP_path = istr.readString();
        inS.endReadParams();
        boolean ret = obj.addSon(iceP_name, iceP_singer, iceP_author, iceP_album, iceP_path, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_deleteSon(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_name;
        iceP_name = istr.readString();
        inS.endReadParams();
        boolean ret = obj.deleteSon(iceP_name, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_searchSon(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_name;
        iceP_name = istr.readString();
        inS.endReadParams();
        Son ret = obj.searchSon(iceP_name, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeValue(ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getSons(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        Son[] ret = obj.getSons(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        SonListHelper.write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_MafactoryMethode(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String[] iceP_s;
        iceP_s = istr.readStringSeq();
        inS.endReadParams();
        obj.MafactoryMethode(iceP_s, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_play(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_name;
        iceP_name = istr.readString();
        inS.endReadParams();
        boolean ret = obj.play(iceP_name, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_stop(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        obj.stop(current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_pause(Player obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        obj.pause(current);
        return inS.setResult(inS.writeEmptyParams());
    }

    final static String[] _iceOps =
    {
        "MafactoryMethode",
        "addSon",
        "deleteSon",
        "getSons",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "pause",
        "play",
        "searchSon",
        "stop"
    };

    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_MafactoryMethode(this, in, current);
            }
            case 1:
            {
                return _iceD_addSon(this, in, current);
            }
            case 2:
            {
                return _iceD_deleteSon(this, in, current);
            }
            case 3:
            {
                return _iceD_getSons(this, in, current);
            }
            case 4:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 5:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 6:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 7:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 8:
            {
                return _iceD_pause(this, in, current);
            }
            case 9:
            {
                return _iceD_play(this, in, current);
            }
            case 10:
            {
                return _iceD_searchSon(this, in, current);
            }
            case 11:
            {
                return _iceD_stop(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}