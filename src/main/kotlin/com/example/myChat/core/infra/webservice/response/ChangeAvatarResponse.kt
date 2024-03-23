package com.example.myChat.core.infra.webservice.response

import com.example.myChat.core.domain.entities.ChangeAvatar

class ChangeAvatarResponse(
        var changeAvatar: ChangeAvatar? = null,
        var error: String? = null
)