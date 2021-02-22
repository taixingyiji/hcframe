import translations from './translationsGerman'

export default function customTranslate(template: any, replacements: any) {
  replacements = replacements || {}
  const trans: any = translations

  // Translate
  template = trans[template] || template

  // Replace
  return template.replace(/{([^}]+)}/g, function(_: any, key: any) {
    let str = replacements[key]
    if (
      trans[replacements[key]] !== null &&
      trans[replacements[key]] !== 'undefined'
    ) {
      str = trans[replacements[key]]
    }
    return str || '{' + key + '}'
  })
}
