const BASE_URL = 'http://localhost:9090/api'

async function handleResponse(res) {
	if (!res.ok) {
		const text = await res.text().catch(() => '')
		throw new Error(text || `HTTP ${res.status}`)
	}
	if (res.status === 204) return null
	return res.json()
}

export async function fetchTasks() {
	const res = await fetch(`${BASE_URL}/tasks`)
	return handleResponse(res)
}

export async function createTask({ title, description, priority = 'MEDIUM' }) {
	const res = await fetch(`${BASE_URL}/tasks`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ title, description, priority }),
	})
	return handleResponse(res)
}

export async function completeTask(id) {
	const res = await fetch(`${BASE_URL}/tasks/${id}/complete`, { method: 'PUT' })
	return handleResponse(res)
}

export async function deleteTask(id) {
	const res = await fetch(`${BASE_URL}/tasks/${id}`, { method: 'DELETE' })
	return handleResponse(res)
}

