package org.backwarden.api.adapters.controller.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CredentialWrapperDTO
{

    URI selfLink;
    List<CredentialDTO> credentialDTOS = new ArrayList<>();
}
