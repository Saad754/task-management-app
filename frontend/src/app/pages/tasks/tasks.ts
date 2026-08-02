import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {TaskService} from '../../services/task';
import {Auth} from '../../services/auth';
import {Task, TaskPriority, TaskStatus} from '../../models/task';

@Component({
  selector: 'app-tasks',
  imports: [FormsModule, RouterLink],
  templateUrl: './tasks.html',
  styleUrl: './tasks.css'
})
export class Tasks implements OnInit {

  private taskService = inject(TaskService);
  private auth = inject(Auth);
  private router = inject(Router);

  tasks = signal<Task[]>([]);
  statusFilter = signal<TaskStatus | ''>('');
  priorityFilter = signal<TaskPriority | ''>('');
  error = signal('');

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    const status = this.statusFilter() || undefined;
    const priority = this.priorityFilter() || undefined;

    this.taskService.getTasks(status, priority).subscribe({
      next: (tasks) => this.tasks.set(tasks),
      error: () => this.error.set('Could not load tasks')
    });
  }

  onStatusChange(): void {
    this.priorityFilter.set('');
    this.loadTasks();
  }

  onPriorityChange(): void {
    this.statusFilter.set('');
    this.loadTasks();
  }

  onDelete(id: number): void {
    this.taskService.deleteTask(id).subscribe({
      next: () => this.loadTasks(),
      error: () => this.error.set('Could not delete the task')
    });
  }

  onLogout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
