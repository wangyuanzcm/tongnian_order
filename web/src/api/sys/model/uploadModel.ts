export interface UploadApiResult {
  success: boolean;
  message: string;
  result: {
    uid: string;
    name: string;
    status: string;
    url: string;
  };
}
