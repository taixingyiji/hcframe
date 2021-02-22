interface IConfig {
  ACTIVTI_URL: string
  BASE_URL: string
}

const CONFIG: IConfig = {
  // ACTIVTI_URL: 'http://192.168.43.42:8080/activiti',
  // BASE_URL: 'http://192.168.43.42:8080/common'
  ACTIVTI_URL: 'http://localhost:8081/activiti',
  BASE_URL: 'http://localhost:8080/common'
}

export default CONFIG
