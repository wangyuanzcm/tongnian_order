const fs = require('fs');
const inputPath = 'd:\\github_repo\\tongnian_order\\source\\json\\sys_order.json';
const outputPath = 'd:\\github_repo\\tongnian_order\\source\\B_goods_type_price.json';
const raw = fs.readFileSync(inputPath, 'utf8');
const data = JSON.parse(raw);
const list = Array.isArray(data.goods_type_price) ? data.goods_type_price : [];
const result = list.map(item => {
  const urls = Array.isArray(item.goods_image_list) ? item.goods_image_list.map(i => i && i.url).filter(Boolean) : [];
  return {
    goods_type: item.goods_type,
    goods_price: item.goods_price,
    goods_image_list: JSON.stringify(urls)
  };
});
fs.writeFileSync(outputPath, JSON.stringify(result, null, 2), 'utf8');