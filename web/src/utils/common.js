/**
 * 过滤对象中属性值是null和undefined的属性
 * @param {Object} obj      待过滤的对象
 * @returns {Object}        新对象
 */
export function filterNullUndefined(obj) {
    return Object.entries(obj).reduce((acc, [key, value]) => {
        if (value !== null && value !== undefined) {
            acc[key] = value;
        }
        return acc;
    }, {});
}
