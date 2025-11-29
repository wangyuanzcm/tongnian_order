<template>
  <div>
    <JImageUpload
      v-if="!cellProps.disabledTable"
      :value="innerImageList"
      :token="originColumn.token"
      :action="uploadAction"
      :limit="originColumn.limit || 9"
      :multiple="originColumn.multiple !== false"
      :dynamicDisabled="originColumn.dynamicDisabled"
      @change="handleChangeUpload"
      v-bind="cellProps"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, watch, computed } from 'vue';
  import { JVxeComponent } from '../../types';
  import { useJVxeCompProps, useJVxeComponent } from '../../hooks/useJVxeComponent';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';

  export default defineComponent({
    name: 'JVxeImageUploadCell',
    components: { JImageUpload },
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const setup = useJVxeComponent(props);
      const { innerValue, originColumn, handleChangeCommon } = setup;

      const innerImageList = ref<any[]>([]);

      /** 上传请求地址 */
      const uploadAction = computed(() => {
        if (!originColumn.value.action) {
          return '';
        } else {
          return originColumn.value.action;
        }
      });

      // 监听内部值变化，转换为图片对象数组
      watch(
        innerValue,
        (val) => {
          if (val && Array.isArray(val)) {
            innerImageList.value = val;
          } else if (val) {
            // 兼容字符串格式："id1,id2|url1,url2"
            try {
              const [idStr, urlStr] = val.split('|');
              const ids = idStr.split(',');
              const urls = urlStr.split(',');
              innerImageList.value = ids.map((id, index) => ({
                id,
                url: urls[index],
                name: urls[index]?.substring(urls[index]?.lastIndexOf('/') + 1) || '',
                sort: index + 1,
                status: 'Y'
              }));
            } catch (e) {
              innerImageList.value = [];
            }
          } else {
            innerImageList.value = [];
          }
        },
        { immediate: true }
      );

      function handleChangeUpload(imageList) {
        // 直接将图片对象数组传递给父组件
        handleChangeCommon(imageList);
      }

      return {
        ...setup,
        innerImageList,
        uploadAction,
        handleChangeUpload,
      };
    },
    // 【组件增强】注释详见：：JVxeComponent.Enhanced
    enhanced: {
      switches: { visible: true },
      getValue: (value) => {
        if (value && Array.isArray(value)) {
          return value;
        }
        return value;
      },
      setValue: (value) => {
        if (value && Array.isArray(value)) {
          return value;
        }
        return value;
      },
    } as JVxeComponent.EnhancedPartial,
  });
</script>