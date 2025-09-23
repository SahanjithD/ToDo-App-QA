import React from 'react'
import TaskRow from './TaskRow.jsx'

export default function TaskList({ tasks, onComplete, onDelete }) {
	if (!tasks.length) {
		return <div className="empty">No tasks yet. Add one above.</div>
	}
	return (
		<ul className="task-list">
			{tasks.map((t) => (
				<TaskRow key={t.id} task={t} onComplete={onComplete} onDelete={onDelete} />
			))}
		</ul>
	)
}
