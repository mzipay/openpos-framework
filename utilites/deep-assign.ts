
// This method assumes you do not have any cyclic references. For the case we plan on using it
// there should be no issues with that since it currently is only being used to copy the JSON
// screen objects into their local versions with out triggering a reference change
export function deepAssign( target, src ) {

    // if target is undefined or null we'll break out and return src
    // if src is null or undefined or primative return the value
    if ( target === undefined || target === null || src === undefined || src === null ||
        typeof src === 'string' || typeof src === 'number' || typeof src === 'boolean') {
        return src;
    }

    // if we are an object we'll iterater our keys, copy our values and return target
    if ( typeof src === 'object') {
        Object.getOwnPropertyNames(src).forEach( prop => {
            target[prop] = deepAssign( target[prop], src[prop]);
        });

        Object.getOwnPropertyNames(target).forEach( prop => {
            if ( !src.hasOwnProperty(prop) ) {
                delete target[prop];
            }
        });
        return target;
    }
}
