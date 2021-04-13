package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils;

import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;

public class EngineConfig {
    VideoEncoderConfiguration.VideoDimensions mVideoDimension;

    int mUid;

    public String mChannel;

    LocalInvitation mInvitation;

    public  RemoteInvitation mRemoteInvitation;

    public void reset() {
        mInvitation = null;
        mRemoteInvitation = null;
        mChannel = null;
    }

    EngineConfig() {
    }
}
