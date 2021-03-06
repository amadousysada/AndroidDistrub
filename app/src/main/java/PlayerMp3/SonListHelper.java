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

package PlayerMp3;

public final class SonListHelper
{
    public static void write(com.zeroc.Ice.OutputStream ostr, Son[] v)
    {
        if(v == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(v.length);
            for(int i0 = 0; i0 < v.length; i0++)
            {
                ostr.writeValue(v[i0]);
            }
        }
    }

    public static Son[] read(com.zeroc.Ice.InputStream istr)
    {
        final Son[] v;
        final int len0 = istr.readAndCheckSeqSize(1);
        v = new Son[len0];
        for(int i0 = 0; i0 < len0; i0++)
        {
            final int fi0 = i0;
            istr.readValue(value -> v[fi0] = value, Son.class);
        }
        return v;
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<Son[]> v)
    {
        if(v != null && v.isPresent())
        {
            write(ostr, tag, v.get());
        }
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, Son[] v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            SonListHelper.write(ostr, v);
            ostr.endSize(pos);
        }
    }

    public static java.util.Optional<Son[]> read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            Son[] v;
            v = SonListHelper.read(istr);
            return java.util.Optional.of(v);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }
}
