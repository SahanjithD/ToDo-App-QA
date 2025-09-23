import React, { useState } from 'react'

export default function TaskForm({ onAdd }) {
	const [title, setTitle] = useState('')
	const [description, setDescription] = useState('')
	const [submitting, setSubmitting] = useState(false)

	async function handleSubmit(e) {
		e.preventDefault()
		if (!title.trim()) return
		setSubmitting(true)
		try {
			await onAdd({ title: title.trim(), description: description.trim() })
			setTitle('')
			setDescription('')
		} finally {
			setSubmitting(false)
		}
	}

	return (
		<form className="task-form" onSubmit={handleSubmit}>
			<div className="row">
				<input
					type="text"
					placeholder="Task title"
					value={title}
					onChange={(e) => setTitle(e.target.value)}
					required
				/>
				<button type="submit" disabled={submitting || !title.trim()}>
					{submitting ? 'Adding...' : 'Add Task'}
				</button>
			</div>
			<textarea
				placeholder="Description (optional)"
				value={description}
				onChange={(e) => setDescription(e.target.value)}
				rows={3}
			/>
		</form>
	)
}
