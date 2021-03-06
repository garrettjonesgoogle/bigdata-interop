/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.hadoop.gcsio;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.auto.value.AutoValue;
import com.google.cloud.hadoop.util.AsyncWriteChannelOptions;
import com.google.cloud.hadoop.util.HttpTransportFactory;
import com.google.cloud.hadoop.util.RequesterPaysOptions;
import javax.annotation.Nullable;

/** Configuration options for the GoogleCloudStorage class. */
@AutoValue
public abstract class GoogleCloudStorageOptions {

  /** Default setting for enabling auto-repair of implicit directories. */
  public static final boolean AUTO_REPAIR_IMPLICIT_DIRECTORIES_DEFAULT = true;

  /** Default setting for enabling inferring of implicit directories. */
  public static final boolean INFER_IMPLICIT_DIRECTORIES_DEFAULT = true;

  /** Default setting for whether or not to create a marker file when beginning file creation. */
  public static final boolean CREATE_EMPTY_MARKER_OBJECT_DEFAULT = false;

  /**
   * Default setting for the length of time to wait for empty objects to appear if we believe we are
   * in a race with multiple workers.
   */
  public static final int MAX_WAIT_MILLIS_FOR_EMPTY_OBJECT_CREATION = 3_000;

  /** Default number of items to return per call to the list* GCS RPCs. */
  public static final long MAX_LIST_ITEMS_PER_CALL_DEFAULT = 1024;

  /** Default setting for maximum number of requests per GCS batch. */
  public static final long MAX_REQUESTS_PER_BATCH_DEFAULT = 30;

  /** Default setting for number of threads to execute GCS batch requests. */
  public static final int BATCH_THREADS_DEFAULT = 0;

  /** Default setting for maximum number of GCS HTTP request retires. */
  public static final int MAX_HTTP_REQUEST_RETRIES = 10;

  /** Default setting for connect timeout (in millisecond) of GCS HTTP request. */
  public static final int HTTP_REQUEST_CONNECT_TIMEOUT = 20 * 1000;

  /** Default setting for read timeout (in millisecond) of GCS HTTP request. */
  public static final int HTTP_REQUEST_READ_TIMEOUT = 20 * 1000;

  /** Default setting for whether or not to use rewrite request for copy operation. */
  public static final boolean COPY_WITH_REWRITE_DEFAULT = false;

  /** Default setting for async write channel. */
  public static final AsyncWriteChannelOptions ASYNC_WRITE_CHANNEL_OPTIONS_DEFAULT =
      AsyncWriteChannelOptions.newBuilder().build();

  /** Default setting for requester pays feature. */
  public static final RequesterPaysOptions REQUESTER_PAYS_OPTIONS_DEFAULT =
      RequesterPaysOptions.DEFAULT;

  public static Builder newBuilder() {
    return new AutoValue_GoogleCloudStorageOptions.Builder()
        .setAutoRepairImplicitDirectoriesEnabled(AUTO_REPAIR_IMPLICIT_DIRECTORIES_DEFAULT)
        .setInferImplicitDirectoriesEnabled(INFER_IMPLICIT_DIRECTORIES_DEFAULT)
        .setCreateMarkerObjects(CREATE_EMPTY_MARKER_OBJECT_DEFAULT)
        .setMaxWaitMillisForEmptyObjectCreation(MAX_WAIT_MILLIS_FOR_EMPTY_OBJECT_CREATION)
        .setMaxListItemsPerCall(MAX_LIST_ITEMS_PER_CALL_DEFAULT)
        .setMaxRequestsPerBatch(MAX_REQUESTS_PER_BATCH_DEFAULT)
        .setBatchThreads(BATCH_THREADS_DEFAULT)
        .setMaxHttpRequestRetries(MAX_HTTP_REQUEST_RETRIES)
        .setHttpRequestConnectTimeout(HTTP_REQUEST_CONNECT_TIMEOUT)
        .setHttpRequestReadTimeout(HTTP_REQUEST_READ_TIMEOUT)
        .setTransportType(HttpTransportFactory.DEFAULT_TRANSPORT_TYPE)
        .setCopyWithRewriteEnabled(COPY_WITH_REWRITE_DEFAULT)
        .setWriteChannelOptions(ASYNC_WRITE_CHANNEL_OPTIONS_DEFAULT)
        .setRequesterPaysOptions(REQUESTER_PAYS_OPTIONS_DEFAULT);
  }

  @Nullable
  public abstract String getProjectId();

  @Nullable
  public abstract String getAppName();

  public abstract boolean isAutoRepairImplicitDirectoriesEnabled();

  public abstract boolean isInferImplicitDirectoriesEnabled();

  public abstract boolean isMarkerFileCreationEnabled();

  public abstract int getMaxWaitMillisForEmptyObjectCreation();

  public abstract long getMaxListItemsPerCall();

  public abstract long getMaxRequestsPerBatch();

  public abstract int getBatchThreads();

  public abstract int getMaxHttpRequestRetries();

  public abstract int getHttpRequestConnectTimeout();

  public abstract int getHttpRequestReadTimeout();

  public abstract HttpTransportFactory.HttpTransportType getTransportType();

  @Nullable
  public abstract String getProxyAddress();

  public abstract boolean isCopyWithRewriteEnabled();

  public abstract AsyncWriteChannelOptions getWriteChannelOptions();

  public abstract RequesterPaysOptions getRequesterPaysOptions();

  public abstract Builder toBuilder();

  public void throwIfNotValid() {
    checkArgument(!isNullOrEmpty(getAppName()), "appName must not be null or empty");
  }

  /** Mutable builder for the {@link GoogleCloudStorageOptions} class. */
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setProjectId(String projectId);

    public abstract Builder setAppName(String appName);

    public abstract Builder setAutoRepairImplicitDirectoriesEnabled(boolean autoRepair);

    public abstract Builder setInferImplicitDirectoriesEnabled(boolean inferImplicitDirectories);

    public abstract Builder setMarkerFileCreationEnabled(boolean markerFileCreationEnabled);

    /** @deprecated use {@link #setMarkerFileCreationEnabled} instead */
    @Deprecated
    public Builder setCreateMarkerObjects(boolean createMarkerObjects) {
      return setMarkerFileCreationEnabled(createMarkerObjects);
    }

    public abstract Builder setMaxWaitMillisForEmptyObjectCreation(int durationMillis);

    public abstract Builder setMaxListItemsPerCall(long maxListItemsPerCall);

    // According to https://developers.google.com/storage/docs/json_api/v1/how-tos/batch
    // there is a maximum of 1000 requests per batch.
    public abstract Builder setMaxRequestsPerBatch(long maxRequestsPerBatch);

    public abstract Builder setBatchThreads(int batchThreads);

    public abstract Builder setMaxHttpRequestRetries(int maxHttpRequestRetries);

    public abstract Builder setHttpRequestConnectTimeout(int httpRequestConnectTimeout);

    public abstract Builder setHttpRequestReadTimeout(int httpRequestReadTimeout);

    public abstract Builder setTransportType(HttpTransportFactory.HttpTransportType transportType);

    public abstract Builder setProxyAddress(String proxyAddress);

    public abstract Builder setCopyWithRewriteEnabled(boolean copyWithRewrite);

    public abstract Builder setWriteChannelOptions(AsyncWriteChannelOptions writeChannelOptions);

    @Deprecated private AsyncWriteChannelOptions.Builder writeChannelOptionsBuilder;

    /** @deprecated use {@link #setWriteChannelOptions} instead */
    @Deprecated
    public Builder setWriteChannelOptionsBuilder(AsyncWriteChannelOptions.Builder builder) {
      writeChannelOptionsBuilder = builder;
      return this;
    }

    /** @deprecated use {@link #setWriteChannelOptions} instead */
    @Deprecated
    public AsyncWriteChannelOptions.Builder getWriteChannelOptionsBuilder() {
      return writeChannelOptionsBuilder == null
          ? writeChannelOptionsBuilder = AsyncWriteChannelOptions.newBuilder()
          : writeChannelOptionsBuilder;
    }

    public abstract Builder setRequesterPaysOptions(RequesterPaysOptions requesterPaysOptions);

    abstract GoogleCloudStorageOptions autoBuild();

    public GoogleCloudStorageOptions build() {
      if (writeChannelOptionsBuilder != null) {
        setWriteChannelOptions(writeChannelOptionsBuilder.build());
      }
      return autoBuild();
    }
  }
}
