/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.oss.minio.rest.request.object;

import cn.herodotus.engine.assistant.core.utils.DateTimeUtils;
import cn.herodotus.oss.minio.rest.definition.ObjectVersionRequest;
import cn.herodotus.oss.minio.rest.request.domain.RetentionRequest;
import io.minio.SetObjectRetentionArgs;
import io.minio.messages.Retention;
import io.minio.messages.RetentionMode;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>Description: SetObjectRetentionRequest </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/4/18 16:03
 */
public class SetObjectRetentionRequest extends ObjectVersionRequest<SetObjectRetentionArgs.Builder, SetObjectRetentionArgs> {

    private RetentionRequest retention;

    private Boolean bypassGovernanceMode = false;

    public RetentionRequest getRetention() {
        return retention;
    }

    public void setRetention(RetentionRequest retention) {
        this.retention = retention;
    }

    public Boolean getBypassGovernanceMode() {
        return bypassGovernanceMode;
    }

    public void setBypassGovernanceMode(Boolean bypassGovernanceMode) {
        this.bypassGovernanceMode = bypassGovernanceMode;
    }

    @Override
    public void prepare(SetObjectRetentionArgs.Builder builder) {
        if (ObjectUtils.isNotEmpty(getRetention())) {
            Retention retention = new Retention(RetentionMode.valueOf(getRetention().getMode()), DateTimeUtils.stringToZonedDateTime(getRetention().getRetainUntilDate()));
            builder.config(retention);
        }
        builder.bypassGovernanceMode(getBypassGovernanceMode());
        super.prepare(builder);
    }

    @Override
    public SetObjectRetentionArgs.Builder getBuilder() {
        return SetObjectRetentionArgs.builder();
    }
}
