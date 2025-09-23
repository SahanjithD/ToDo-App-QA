import React, { useEffect, useMemo, useState } from 'react'
import { fetchTasks, createTask, completeTask, deleteTask } from './api.js'
import TaskForm from './components/TaskForm.jsx'
import TaskList from './components/TaskList.jsx'

export default function App() {
	const [tasks, setTasks] = useState([])
	const [loading, setLoading] = useState(true)
	const [error, setError] = useState('')

	const sortedTasks = useMemo(() => {
		const copy = [...tasks]
		copy.sort((a, b) => {
			if (a.completed === b.completed) return b.id - a.id
			return a.completed ? 1 : -1
		})
		return copy
	}, [tasks])

	useEffect(() => {
		let isMounted = true
		setLoading(true)
		fetchTasks()
			.then((data) => {
				if (!isMounted) return
				setTasks(data)
			})
			.catch((err) => {
				console.error(err)
				if (!isMounted) return
				setError('Failed to load tasks')
			})
			.finally(() => {
				if (!isMounted) return
				setLoading(false)
			})
		return () => {
			isMounted = false
		}
	}, [])

	async function handleAdd({ title, description }) {
		setError('')
		try {
			const newTask = await createTask({ title, description })
			setTasks((prev) => [newTask, ...prev])
		} catch (err) {
			console.error(err)
			setError('Failed to create task')
		}
	}

	async function handleComplete(id) {
		setError('')
		try {
			await completeTask(id)
			setTasks((prev) => prev.map((t) => (t.id === id ? { ...t, completed: true } : t)))
		} catch (err) {
			console.error(err)
			setError('Failed to complete task')
		}
	}

	async function handleDelete(id) {
		setError('')
		try {
			await deleteTask(id)
			setTasks((prev) => prev.filter((t) => t.id !== id))
		} catch (err) {
			console.error(err)
			setError('Failed to delete task')
		}
	}

	return (
		<div className="container">
			<h1>To-Do List</h1>
			<TaskForm onAdd={handleAdd} />
			{error && <div className="error">{error}</div>}
			{loading ? <div>Loading...</div> : <TaskList tasks={sortedTasks} onComplete={handleComplete} onDelete={handleDelete} />}
		</div>
	)
}
