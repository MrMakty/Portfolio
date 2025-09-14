export enum DockStatus {
  AVAILABLE = 'AVAILABLE',
  RESERVED = 'RESERVED',
  MAINTENANCE = 'MAINTENANCE'
}

export interface DockStatusDTO {
  status: DockStatus;
  reservedUntil: Date | null;
  availableFrom: Date | null;
}
