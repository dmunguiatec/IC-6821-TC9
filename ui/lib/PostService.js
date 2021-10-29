export async function getPosts() {
    console.log(`[getPosts()] Fetching posts`)

    const userRes = await fetch(`http://localhost:8080/api/users/Bret`)
    if (!userRes.ok) {
        console.log(`[getPosts()] Failed fetching data: User not found`)
        return []
    }
    const user = await userRes.json()
    console.log(`[getPosts()] ${userRes.status} User found ${user.extId}`)
    
    const res = await fetch(`http://localhost:8080/api/users/${user.extId}/posts`)

    if (!res.ok) {
        console.log(`[getPosts()] Failed fetching data: ${res.status} ${res.statusText}`)
        return []
    }

    const posts = await res.json()
    return posts
}


export async function getPost(extId) {
    const res = await fetch(`http://localhost:8080/api/posts/${extId}`)

    if (!res.ok) {
        console.log(`[getPost(${extId})] Failed fetching data: ${res.status} ${res.statusText}`)
        return []
    }

    const post = await res.json()
    return post
}
