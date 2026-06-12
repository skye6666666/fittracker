// export function parseJwt(token) {
//   if (!token) return null

//   try {
//     const base64Payload = token.split('.')[1]
//     const payload = JSON.parse(atob(base64Payload))
//     return payload
//   } catch (e) {
//     console.error("Invalid token", e)
//     return null
//   }
// }


export function parseJwt(token) {
  if (!token) return null

  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch (e) {
    console.error("Invalid token", e)
    return null
  }
}

export function getUser() {
  const token = localStorage.getItem("token")
  return parseJwt(token)
}

export function getRole() {
  return getUser()?.role
}

export function logout() {

  localStorage.removeItem("token")
  localStorage.removeItem("email")
  localStorage.removeItem("role")

  window.location.href = "/"

}