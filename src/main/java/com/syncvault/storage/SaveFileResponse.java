package com.syncvault.storage;

import io.minio.ObjectWriteResponse;

import java.util.UUID;

public record SaveFileResponse(ObjectWriteResponse response, UUID file_id) {
}
